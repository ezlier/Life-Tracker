package ezria.lifetrackr.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PresenceRegistry {
    private final Map<String, ConnectionPresence> connections = new ConcurrentHashMap<>();
    private final Map<Long, FocusPresence> focuses = new ConcurrentHashMap<>();

    public void connect(String connectionId, Long userId, long now) {
        connections.put(connectionId, new ConnectionPresence(connectionId, userId, now));
    }

    public void bind(String connectionId, String clientId, Long focusId, long now) {
        ConnectionPresence connection = requireConnection(connectionId);
        if (connection.focusId != null && !connection.focusId.equals(focusId)) {
            unbindInternal(connection, now);
        }

        FocusPresence focus = focuses.compute(focusId, (id, existing) -> {
            if (existing == null) {
                return new FocusPresence(focusId, connection.userId);
            }
            if (!existing.userId.equals(connection.userId)) {
                throw new IllegalArgumentException("Focus session belongs to another user");
            }
            return existing;
        });

        synchronized (focus) {
            connection.clientId = clientId;
            connection.focusId = focusId;
            connection.lastSeenAt = now;
            focus.connectionIds.add(connectionId);
            focus.suspectSince = null;
        }
    }

    public void heartbeat(String connectionId, Long requestedFocusId, long now) {
        ConnectionPresence connection = requireConnection(connectionId);
        Long boundFocusId = connection.focusId;
        if (boundFocusId == null) {
            throw new IllegalStateException("Connection is not bound to a focus session");
        }
        if (requestedFocusId != null && !boundFocusId.equals(requestedFocusId)) {
            throw new IllegalArgumentException("Heartbeat focusId does not match the bound focus session");
        }

        touch(connectionId, now);
    }

    public void touch(String connectionId, long now) {
        ConnectionPresence connection = requireConnection(connectionId);
        Long boundFocusId = connection.focusId;
        connection.lastSeenAt = now;
        if (boundFocusId == null) {
            return;
        }

        FocusPresence focus = focuses.get(boundFocusId);
        if (focus == null) {
            connection.focusId = null;
            throw new IllegalStateException("Focus presence has already been closed");
        }

        synchronized (focus) {
            connection.lastSeenAt = now;
            focus.connectionIds.add(connectionId);
            focus.suspectSince = null;
        }
    }

    public void unbind(String connectionId, long now) {
        ConnectionPresence connection = requireConnection(connectionId);
        unbindInternal(connection, now);
    }

    public void disconnect(String connectionId, long now) {
        ConnectionPresence connection = connections.remove(connectionId);
        if (connection != null) {
            unbindInternal(connection, now);
        }
    }

    public Long getBoundFocusId(String connectionId) {
        ConnectionPresence connection = connections.get(connectionId);
        return connection == null ? null : connection.focusId;
    }

    public void trackRecoveryCandidate(Long userId, Long focusId, long suspectSince) {
        focuses.compute(focusId, (id, existing) -> {
            if (existing == null) {
                FocusPresence candidate = new FocusPresence(focusId, userId);
                candidate.suspectSince = suspectSince;
                return candidate;
            }
            if (!existing.userId.equals(userId)) {
                throw new IllegalStateException("Conflicting owner for focus session " + focusId);
            }
            return existing;
        });
    }

    public void stopTracking(Long focusId) {
        FocusPresence focus = focuses.remove(focusId);
        if (focus == null) {
            return;
        }

        synchronized (focus) {
            for (String connectionId : focus.connectionIds) {
                ConnectionPresence connection = connections.get(connectionId);
                if (connection != null && focusId.equals(connection.focusId)) {
                    connection.focusId = null;
                }
            }
            focus.connectionIds.clear();
        }
    }

    public void processOfflineFocuses(long now,
                                      long heartbeatTimeoutMs,
                                      long offlineGraceMs,
                                      OfflineAction offlineAction) {
        for (Map.Entry<Long, FocusPresence> entry : new ArrayList<>(focuses.entrySet())) {
            FocusPresence focus = entry.getValue();
            boolean removeFocus = false;

            synchronized (focus) {
                focus.connectionIds.removeIf(connectionId -> {
                    ConnectionPresence connection = connections.get(connectionId);
                    return connection == null || !focus.focusId.equals(connection.focusId);
                });

                boolean healthy = false;
                long latestStaleAt = Long.MIN_VALUE;
                for (String connectionId : focus.connectionIds) {
                    ConnectionPresence connection = connections.get(connectionId);
                    if (connection == null) {
                        continue;
                    }
                    long staleAt = connection.lastSeenAt + heartbeatTimeoutMs;
                    latestStaleAt = Math.max(latestStaleAt, staleAt);
                    if (now < staleAt) {
                        healthy = true;
                        break;
                    }
                }

                if (healthy) {
                    focus.suspectSince = null;
                    continue;
                }

                if (focus.suspectSince == null) {
                    focus.suspectSince = latestStaleAt == Long.MIN_VALUE ? now : latestStaleAt;
                }
                if (now < focus.suspectSince + offlineGraceMs) {
                    continue;
                }

                try {
                    offlineAction.autoPause(focus.userId, focus.focusId);
                    for (String connectionId : focus.connectionIds) {
                        ConnectionPresence connection = connections.get(connectionId);
                        if (connection != null && focus.focusId.equals(connection.focusId)) {
                            connection.focusId = null;
                        }
                    }
                    focus.connectionIds.clear();
                    removeFocus = true;
                } catch (RuntimeException exception) {
                    log.error("Failed to process offline focus session: userId={}, focusId={}",
                            focus.userId, focus.focusId, exception);
                    focus.suspectSince = now;
                }
            }

            if (removeFocus) {
                focuses.remove(entry.getKey(), focus);
            }
        }
    }

    private void unbindInternal(ConnectionPresence connection, long now) {
        Long focusId = connection.focusId;
        connection.focusId = null;
        if (focusId == null) {
            return;
        }

        FocusPresence focus = focuses.get(focusId);
        if (focus == null) {
            return;
        }
        synchronized (focus) {
            focus.connectionIds.remove(connection.connectionId);
            if (focus.connectionIds.isEmpty()) {
                focus.suspectSince = now;
            }
        }
    }

    private ConnectionPresence requireConnection(String connectionId) {
        ConnectionPresence connection = connections.get(connectionId);
        if (connection == null) {
            throw new IllegalStateException("WebSocket connection is not registered");
        }
        return connection;
    }

    @FunctionalInterface
    public interface OfflineAction {
        boolean autoPause(Long userId, Long focusId);
    }

    private static final class ConnectionPresence {
        private final String connectionId;
        private final Long userId;
        private volatile String clientId;
        private volatile Long focusId;
        private volatile long lastSeenAt;

        private ConnectionPresence(String connectionId, Long userId, long lastSeenAt) {
            this.connectionId = connectionId;
            this.userId = userId;
            this.lastSeenAt = lastSeenAt;
        }
    }

    private static final class FocusPresence {
        private final Long focusId;
        private final Long userId;
        private final Set<String> connectionIds = new HashSet<>();
        private Long suspectSince;

        private FocusPresence(Long focusId, Long userId) {
            this.focusId = focusId;
            this.userId = userId;
        }
    }
}

package ezria.lifetrackr.websocket;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PresenceRegistryTests {
    private static final long HEARTBEAT_TIMEOUT = 25_000;
    private static final long OFFLINE_GRACE = 15_000;

    @Test
    void pausesOnceAfterDisconnectedConnectionExceedsGracePeriod() {
        PresenceRegistry registry = new PresenceRegistry();
        List<String> pauses = new ArrayList<>();

        registry.connect("connection-1", 7L, 1_000);
        registry.bind("connection-1", "client-1", 42L, 1_000);
        registry.disconnect("connection-1", 2_000);

        registry.processOfflineFocuses(16_999, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(userId + ":" + focusId));
        assertTrue(pauses.isEmpty());

        registry.processOfflineFocuses(17_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(userId + ":" + focusId));
        registry.processOfflineFocuses(50_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(userId + ":" + focusId));

        assertEquals(List.of("7:42"), pauses);
    }

    @Test
    void keepsFocusRunningWhileAnotherDeviceIsHealthy() {
        PresenceRegistry registry = new PresenceRegistry();
        List<Long> pauses = new ArrayList<>();

        registry.connect("connection-1", 7L, 1_000);
        registry.bind("connection-1", "client-1", 42L, 1_000);
        registry.connect("connection-2", 7L, 1_000);
        registry.bind("connection-2", "client-2", 42L, 1_000);

        registry.disconnect("connection-1", 2_000);
        registry.heartbeat("connection-2", 42L, 20_000);
        registry.processOfflineFocuses(30_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(focusId));

        assertTrue(pauses.isEmpty());
    }

    @Test
    void reconnectDuringGraceCancelsOfflineDecision() {
        PresenceRegistry registry = new PresenceRegistry();
        List<Long> pauses = new ArrayList<>();

        registry.connect("connection-1", 7L, 1_000);
        registry.bind("connection-1", "client-1", 42L, 1_000);
        registry.disconnect("connection-1", 2_000);

        registry.connect("connection-2", 7L, 10_000);
        registry.bind("connection-2", "client-1", 42L, 10_000);
        registry.processOfflineFocuses(17_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(focusId));

        assertTrue(pauses.isEmpty());
    }

    @Test
    void staleHeartbeatStartsGraceAtHeartbeatTimeout() {
        PresenceRegistry registry = new PresenceRegistry();
        List<Long> pauses = new ArrayList<>();

        registry.connect("connection-1", 7L, 1_000);
        registry.bind("connection-1", "client-1", 42L, 1_000);

        registry.processOfflineFocuses(26_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(focusId));
        assertTrue(pauses.isEmpty());

        registry.processOfflineFocuses(41_000, HEARTBEAT_TIMEOUT, OFFLINE_GRACE,
                (userId, focusId) -> pauses.add(focusId));
        assertEquals(List.of(42L), pauses);
    }
}

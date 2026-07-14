package ezria.lifetrackr.websocket;

import ezria.lifetrackr.Config.PresenceProperties;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.service.FocusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
public class OfflineDetector {
    private final PresenceRegistry presenceRegistry;
    private final FocusService focusService;
    private final PresenceProperties properties;
    private final long startedAt = System.currentTimeMillis();
    private final AtomicBoolean recoveryLoaded = new AtomicBoolean(false);

    public OfflineDetector(PresenceRegistry presenceRegistry,
                           FocusService focusService,
                           PresenceProperties properties) {
        this.presenceRegistry = presenceRegistry;
        this.focusService = focusService;
        this.properties = properties;
    }

    @Scheduled(fixedDelayString = "${presence.scan-interval-ms:5000}")
    public void detectOfflineClients() {
        long now = System.currentTimeMillis();
        loadRecoveryCandidatesIfDue(now);
        presenceRegistry.processOfflineFocuses(
                now,
                properties.getHeartbeatTimeoutMs(),
                properties.getOfflineGraceMs(),
                this::autoPause
        );
    }

    private void loadRecoveryCandidatesIfDue(long now) {
        if (recoveryLoaded.get() || now - startedAt < properties.getStartupRecoveryDelayMs()) {
            return;
        }

        try {
            long suspectSince = startedAt + properties.getHeartbeatTimeoutMs();
            for (FocusSession session : focusService.getRunningFocusSessionsForRecovery()) {
                presenceRegistry.trackRecoveryCandidate(
                        session.getUserId(),
                        session.getId(),
                        suspectSince
                );
            }
            recoveryLoaded.set(true);
        } catch (RuntimeException exception) {
            log.error("Failed to load running focus sessions for presence recovery", exception);
        }
    }

    private boolean autoPause(Long userId, Long focusId) {
        boolean paused = focusService.autoPauseFocusSession(userId, focusId);
        if (paused) {
            log.info("Automatically paused focus session because all clients are offline: userId={}, focusId={}",
                    userId, focusId);
        }
        return paused;
    }
}

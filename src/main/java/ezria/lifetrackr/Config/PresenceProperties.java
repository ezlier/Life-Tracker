package ezria.lifetrackr.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "presence")
public class PresenceProperties {
    private long serverPingIntervalMs = 10_000;
    private long heartbeatTimeoutMs = 25_000;
    private long offlineGraceMs = 15_000;
    private long scanIntervalMs = 5_000;
    private long ticketTtlMs = 30_000;
    private long startupRecoveryDelayMs = 45_000;
    private List<String> allowedOriginPatterns = new ArrayList<>(List.of(
            "http://localhost:*",
            "http://127.0.0.1:*"
    ));
}

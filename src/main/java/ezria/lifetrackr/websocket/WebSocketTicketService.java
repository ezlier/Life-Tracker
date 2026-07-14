package ezria.lifetrackr.websocket;

import ezria.lifetrackr.Config.PresenceProperties;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketTicketService {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final Map<String, TicketEntry> tickets = new ConcurrentHashMap<>();
    private final PresenceProperties properties;

    public WebSocketTicketService(PresenceProperties properties) {
        this.properties = properties;
    }

    public String issue(Long userId) {
        long now = System.currentTimeMillis();
        tickets.entrySet().removeIf(entry -> entry.getValue().expiresAt() <= now);

        while (true) {
            byte[] bytes = new byte[32];
            SECURE_RANDOM.nextBytes(bytes);
            String ticket = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            TicketEntry entry = new TicketEntry(userId, now + properties.getTicketTtlMs());
            if (tickets.putIfAbsent(ticket, entry) == null) {
                return ticket;
            }
        }
    }

    public Long consume(String ticket) {
        if (ticket == null || ticket.isBlank()) {
            return null;
        }

        TicketEntry entry = tickets.remove(ticket);
        if (entry == null || entry.expiresAt() <= System.currentTimeMillis()) {
            return null;
        }
        return entry.userId();
    }

    private record TicketEntry(Long userId, long expiresAt) {
    }
}

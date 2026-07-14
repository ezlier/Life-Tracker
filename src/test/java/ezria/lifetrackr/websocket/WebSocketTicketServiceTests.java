package ezria.lifetrackr.websocket;

import ezria.lifetrackr.Config.PresenceProperties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class WebSocketTicketServiceTests {
    @Test
    void ticketCanOnlyBeConsumedOnce() {
        PresenceProperties properties = new PresenceProperties();
        properties.setTicketTtlMs(30_000);
        WebSocketTicketService service = new WebSocketTicketService(properties);

        String ticket = service.issue(9L);

        assertEquals(9L, service.consume(ticket));
        assertNull(service.consume(ticket));
    }
}

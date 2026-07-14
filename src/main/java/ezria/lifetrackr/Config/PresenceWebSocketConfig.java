package ezria.lifetrackr.Config;

import ezria.lifetrackr.websocket.PresenceWebSocketHandler;
import ezria.lifetrackr.websocket.WebSocketTicketHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@EnableScheduling
public class PresenceWebSocketConfig implements WebSocketConfigurer {
    private final PresenceWebSocketHandler presenceWebSocketHandler;
    private final WebSocketTicketHandshakeInterceptor ticketHandshakeInterceptor;
    private final PresenceProperties properties;

    public PresenceWebSocketConfig(PresenceWebSocketHandler presenceWebSocketHandler,
                                   WebSocketTicketHandshakeInterceptor ticketHandshakeInterceptor,
                                   PresenceProperties properties) {
        this.presenceWebSocketHandler = presenceWebSocketHandler;
        this.ticketHandshakeInterceptor = ticketHandshakeInterceptor;
        this.properties = properties;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(presenceWebSocketHandler, "/ws/presence")
                .addInterceptors(ticketHandshakeInterceptor)
                .setAllowedOriginPatterns(properties.getAllowedOriginPatterns().toArray(String[]::new));
    }
}

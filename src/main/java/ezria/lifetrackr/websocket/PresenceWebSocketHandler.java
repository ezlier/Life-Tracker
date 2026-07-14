package ezria.lifetrackr.websocket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ezria.lifetrackr.service.FocusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PingMessage;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class PresenceWebSocketHandler extends TextWebSocketHandler {
    private static final int MAX_MESSAGE_SIZE = 4_096;
    private static final int MAX_CLIENT_ID_LENGTH = 128;

    private final PresenceRegistry presenceRegistry;
    private final FocusService focusService;
    private final ObjectMapper objectMapper;
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public PresenceWebSocketHandler(PresenceRegistry presenceRegistry,
                                    FocusService focusService,
                                    ObjectMapper objectMapper) {
        this.presenceRegistry = presenceRegistry;
        this.focusService = focusService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserId(session);
        if (userId == null) {
            session.close(CloseStatus.POLICY_VIOLATION.withReason("Unauthenticated WebSocket connection"));
            return;
        }

        presenceRegistry.connect(session.getId(), userId, System.currentTimeMillis());
        sessions.put(session.getId(), session);
        send(session, Map.of(
                "type", "CONNECTED",
                "connectionId", session.getId(),
                "serverTime", System.currentTimeMillis()
        ));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        if (message.getPayloadLength() > MAX_MESSAGE_SIZE) {
            sendError(session, "MESSAGE_TOO_LARGE", "Message exceeds 4096 bytes");
            return;
        }

        try {
            JsonNode payload = objectMapper.readTree(message.getPayload());
            String type = requiredText(payload, "type").toUpperCase();
            switch (type) {
                case "BIND_FOCUS" -> bindFocus(session, payload);
                case "HEARTBEAT" -> heartbeat(session, payload);
                case "UNBIND_FOCUS" -> unbindFocus(session);
                default -> sendError(session, "UNKNOWN_MESSAGE_TYPE", "Unsupported message type: " + type);
            }
        } catch (IllegalArgumentException | IllegalStateException exception) {
            sendError(session, "INVALID_MESSAGE", exception.getMessage());
        } catch (IOException exception) {
            sendError(session, "INVALID_JSON", "Message must be valid JSON");
        }
    }

    private void bindFocus(WebSocketSession session, JsonNode payload) throws IOException {
        Long userId = getUserId(session);
        Long focusId = requiredLong(payload, "focusId");
        String clientId = requiredText(payload, "clientId");
        if (clientId.length() > MAX_CLIENT_ID_LENGTH) {
            throw new IllegalArgumentException("clientId is too long");
        }
        if (!focusService.isRunningFocusSession(userId, focusId)) {
            sendError(session, "FOCUS_NOT_RUNNING", "Focus session does not exist, is not owned by the user, or is not running");
            return;
        }

        presenceRegistry.bind(session.getId(), clientId, focusId, System.currentTimeMillis());
        if (!focusService.isRunningFocusSession(userId, focusId)) {
            presenceRegistry.unbind(session.getId(), System.currentTimeMillis());
            sendError(session, "FOCUS_NOT_RUNNING", "Focus session is no longer running");
            return;
        }

        send(session, Map.of(
                "type", "FOCUS_BOUND",
                "focusId", focusId,
                "serverTime", System.currentTimeMillis()
        ));
    }

    private void heartbeat(WebSocketSession session, JsonNode payload) throws IOException {
        Long focusId = optionalLong(payload, "focusId");
        presenceRegistry.heartbeat(session.getId(), focusId, System.currentTimeMillis());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", "HEARTBEAT_ACK");
        if (payload.has("seq")) {
            response.put("seq", payload.get("seq"));
        }
        response.put("focusId", presenceRegistry.getBoundFocusId(session.getId()));
        response.put("serverTime", System.currentTimeMillis());
        send(session, response);
    }

    private void unbindFocus(WebSocketSession session) throws IOException {
        Long focusId = presenceRegistry.getBoundFocusId(session.getId());
        presenceRegistry.unbind(session.getId(), System.currentTimeMillis());
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("type", "FOCUS_UNBOUND");
        response.put("focusId", focusId);
        response.put("serverTime", System.currentTimeMillis());
        send(session, response);
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        try {
            presenceRegistry.touch(session.getId(), System.currentTimeMillis());
        } catch (IllegalStateException ignored) {
            // The close callback may have removed the connection first.
        }
    }

    @Scheduled(fixedDelayString = "${presence.server-ping-interval-ms:10000}")
    public void sendServerPings() {
        byte[] timestamp = ByteBuffer.allocate(Long.BYTES)
                .putLong(System.currentTimeMillis())
                .array();
        for (WebSocketSession session : sessions.values()) {
            if (!session.isOpen()) {
                sessions.remove(session.getId(), session);
                presenceRegistry.disconnect(session.getId(), System.currentTimeMillis());
                continue;
            }
            try {
                synchronized (session) {
                    session.sendMessage(new PingMessage(ByteBuffer.wrap(timestamp)));
                }
            } catch (IOException | IllegalStateException exception) {
                log.debug("Failed to ping presence WebSocket: connectionId={}", session.getId(), exception);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        presenceRegistry.disconnect(session.getId(), System.currentTimeMillis());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.debug("Presence WebSocket transport error: connectionId={}", session.getId(), exception);
        sessions.remove(session.getId());
        presenceRegistry.disconnect(session.getId(), System.currentTimeMillis());
        if (session.isOpen()) {
            session.close(CloseStatus.SERVER_ERROR);
        }
    }

    private Long getUserId(WebSocketSession session) {
        Object value = session.getAttributes().get(WebSocketTicketHandshakeInterceptor.USER_ID_ATTRIBUTE);
        return value instanceof Number number ? number.longValue() : null;
    }

    private String requiredText(JsonNode payload, String field) {
        JsonNode value = payload.get(field);
        if (value == null || !value.isTextual() || value.asText().isBlank()) {
            throw new IllegalArgumentException(field + " is required");
        }
        return value.asText();
    }

    private Long requiredLong(JsonNode payload, String field) {
        Long value = optionalLong(payload, field);
        if (value == null || value <= 0) {
            throw new IllegalArgumentException(field + " must be a positive integer");
        }
        return value;
    }

    private Long optionalLong(JsonNode payload, String field) {
        JsonNode value = payload.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        if (!value.canConvertToLong()) {
            throw new IllegalArgumentException(field + " must be an integer");
        }
        return value.longValue();
    }

    private void sendError(WebSocketSession session, String code, String message) throws IOException {
        send(session, Map.of(
                "type", "ERROR",
                "code", code,
                "message", message == null ? "Invalid request" : message,
                "serverTime", System.currentTimeMillis()
        ));
    }

    private void send(WebSocketSession session, Map<String, ?> payload) throws IOException {
        if (session.isOpen()) {
            synchronized (session) {
                session.sendMessage(new TextMessage(objectMapper.writeValueAsString(payload)));
            }
        }
    }
}

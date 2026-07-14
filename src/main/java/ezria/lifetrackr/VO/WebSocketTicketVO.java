package ezria.lifetrackr.VO;

public record WebSocketTicketVO(String ticket, long expiresInMs, String endpoint) {
}

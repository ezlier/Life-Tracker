package ezria.lifetrackr.Controller;

import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.Config.PresenceProperties;
import ezria.lifetrackr.VO.WebSocketTicketVO;
import ezria.lifetrackr.websocket.WebSocketTicketService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebSocketTicketController {
    private final WebSocketTicketService ticketService;
    private final PresenceProperties properties;

    public WebSocketTicketController(WebSocketTicketService ticketService,
                                     PresenceProperties properties) {
        this.ticketService = ticketService;
        this.properties = properties;
    }

    @PostMapping("/ws-ticket")
    public Result issueTicket(@CurrentUserId Long userId) {
        String ticket = ticketService.issue(userId);
        return Result.success(new WebSocketTicketVO(
                ticket,
                properties.getTicketTtlMs(),
                "/ws/presence"
        ));
    }
}

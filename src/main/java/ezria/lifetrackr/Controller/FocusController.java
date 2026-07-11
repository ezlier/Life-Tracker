package ezria.lifetrackr.Controller;

import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.DTO.FocusSessionDTO;
import ezria.lifetrackr.service.FocusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/focus")
public class FocusController {
    @Autowired
    private FocusService focusService;

    @PostMapping()
    public Result startFocusSession( @CurrentUserId Long userId,
            @RequestBody FocusSessionDTO focusSessionDTO) {
        focusSessionDTO.setUserId(userId);
        log.info("Starting focus session for user: {}", userId);
        Integer SessionId = focusService.startFocusSession(focusSessionDTO);
        return Result.success( SessionId );
    }

    @PatchMapping("/{id}/pause")
    public Result pauseFocusSession(@PathVariable Long id) {
        focusService.pauseFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/resume")
    public Result resumeFocusSession(@PathVariable Long id) {
        focusService.resumeFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/complete")
    public Result completeFocusSession(@PathVariable Long id) {
        focusService.completeFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/cancel")
    public Result cancelFocusSession(@PathVariable Long id) {
        focusService.cancelFocusSession(id);
        return Result.success();
    }
}

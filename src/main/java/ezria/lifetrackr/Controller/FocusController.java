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
        log.info("Pausing focus session with id: {}", id);
        focusService.pauseFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/resume")
    public Result resumeFocusSession(@PathVariable Long id) {
        log.info("Resuming focus session with id: {}", id);
        focusService.resumeFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/complete")
    public Result completeFocusSession(@PathVariable Long id) {
        log.info("Completing focus session with id: {}", id);
        focusService.completeFocusSession(id);
        return Result.success();
    }

    @PatchMapping("/{id}/cancel")
    public Result cancelFocusSession(@PathVariable Long id) {
        log.info("Canceling focus session with id: {}", id);
        focusService.cancelFocusSession(id);
        return Result.success();
    }

    @GetMapping
    public Result getFocusSessions(@CurrentUserId Long userId,
                                    @RequestParam(required = false) String mode,
                                    @RequestParam(required = false) String goal,
                                    @RequestParam(required = false) String status,
                                    @RequestParam(required = false) Boolean isCompleted,
                                    @RequestParam(defaultValue = "1") Integer pageNum,
                                    @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("Fetching focus sessions: userId={}, mode={}, goal={}, status={}, isCompleted={}", userId, mode, goal, status, isCompleted);
        return Result.success(focusService.getFocusSessions(userId, mode, goal, status, isCompleted, pageNum, pageSize));
    }

    @GetMapping("/running")
    public Result getRunningFocusSessions(@CurrentUserId Long userId) {
        log.info("Fetching running focus sessions for user: {}", userId);
        return Result.success(focusService.getFocusSessions(userId, null, null, null, false, 1, 10));
    }
}

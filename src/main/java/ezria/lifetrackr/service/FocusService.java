package ezria.lifetrackr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.FocusSessionDTO;
import ezria.lifetrackr.Entity.FocusSession;

import java.util.List;

public interface FocusService {
    Integer startFocusSession(FocusSessionDTO focusSessionDTO);

    void pauseFocusSession(Long id);

    void resumeFocusSession(Long id);

    void completeFocusSession(Long id);

    void cancelFocusSession(Long id);

    boolean isRunningFocusSession(Long userId, Long id);

    boolean autoPauseFocusSession(Long userId, Long id);

    List<FocusSession> getRunningFocusSessionsForRecovery();

    Page<FocusSession> getFocusSessions(Long userId, String mode, String goal, String status,
                                         Boolean isCompleted, Integer pageNum, Integer pageSize);
}

package ezria.lifetrackr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.VO.TimeLineEventVO;

import java.time.LocalDate;

public interface TimeLineEventService {
    void saveItemEvent(ItemDTO itemDTO, Long itemId);

    void createLog(ItemDTO itemDTO, Long id);

    Page<TimeLineEventVO> getTimeLineEvents(Long userId, Long itemId, String eventType,String itemType,
                                             LocalDate startDate, LocalDate endDate,
                                             Integer pageNum, Integer pageSize);

    void saveFocusSessionEvent(FocusSession focusSession, Long focusSessionId);

    void completedFocusSessionEvent(FocusSession session);

    void cancelFocusSessionEvent(FocusSession session);

    void saveLoginEvent(Integer id);
}

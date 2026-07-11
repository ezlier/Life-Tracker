package ezria.lifetrackr.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.VO.TimeLineEventVO;
import ezria.lifetrackr.service.TimeLineEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/timelineEvent")
public class TimeLineEventController {

    @Autowired
    private TimeLineEventService timeLineEventService;

    @GetMapping
    public Result getTimeLineEvents(@CurrentUserId Long userId,
                                     @RequestParam(required = false) Long itemId,
                                     @RequestParam(required = false) String eventType,
                                     @RequestParam(required = false) String itemType,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                     @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                     @RequestParam(defaultValue = "1") Integer pageNum,
                                     @RequestParam(defaultValue = "10") Integer pageSize) {

        log.info("Getting timeline events: userId={}, itemId={}, eventType={}, itemType={}, startDate={}, endDate={}",
                 userId, itemId, eventType, itemType, startDate, endDate);

        Page<TimeLineEventVO> page = timeLineEventService.getTimeLineEvents(
                userId, itemId, eventType, itemType, startDate, endDate, pageNum, pageSize);
        return Result.success(page);
    }
}

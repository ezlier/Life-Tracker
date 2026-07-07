package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Entity.TimeLineEvent;
import ezria.lifetrackr.Entity.TimelineEventType;
import ezria.lifetrackr.Handler.ChangeHandler;
import ezria.lifetrackr.Mapper.ItemMapper;
import ezria.lifetrackr.Mapper.TimeLineEventMapper;
import ezria.lifetrackr.VO.TimeLineEventVO;
import ezria.lifetrackr.service.TimeLineEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimeLineEventServiceImpl implements TimeLineEventService {

    @Autowired
    private TimeLineEventMapper timeLineEventMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private List<ChangeHandler> handlers;

    @Override
    public void save(ItemDTO itemDTO, Long itemId) {
        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(itemDTO.getUserId());
        event.setItemId(itemId);
        event.setEventType(TimelineEventType.CREATE_ITEM);
        event.setDescription(itemDTO.getTitle());
        timeLineEventMapper.eventInsert(event);
    }

    @Override
    public void createLog(ItemDTO itemDTO, Long itemId) {
        Item oldItem = itemMapper.selectByIdAndUser(itemDTO.getUserId(), itemId);
        if (oldItem == null) {
            return;
        }
        for (ChangeHandler handler : handlers) {
            TimeLineEvent event = handler.handle(oldItem, itemDTO);
            if (event != null) {
                timeLineEventMapper.eventInsert(event);
            }
        }
    }

    @Override
    public Page<TimeLineEventVO> getTimeLineEvents(Long userId, Long itemId, String eventType,
                                                    LocalDate startDate, LocalDate endDate,
                                                    Integer pageNum, Integer pageSize) {
        Page<TimeLineEvent> page = new Page<>(pageNum, pageSize);

        QueryWrapper<TimeLineEvent> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        if (itemId != null) {
            wrapper.eq("item_id", itemId);
        }
        if (eventType != null && !eventType.isEmpty()) {
            wrapper.eq("event_type", eventType);
        }
        if (startDate != null) {
            wrapper.ge("created_at", startDate.atStartOfDay());
        }
        if (endDate != null) {
            wrapper.le("created_at", endDate.plusDays(1).atStartOfDay());
        }
        wrapper.orderByDesc("created_at");

        Page<TimeLineEvent> eventPage = timeLineEventMapper.selectPage(page, wrapper);

        List<TimeLineEventVO> voList = eventPage.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        Page<TimeLineEventVO> voPage = new Page<>(pageNum, pageSize, eventPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    private TimeLineEventVO toVO(TimeLineEvent event) {
        TimeLineEventVO vo = new TimeLineEventVO();
        BeanUtils.copyProperties(event, vo);
        vo.setEventType(event.getEventType().name());
        return vo;
    }
}

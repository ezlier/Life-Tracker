package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Entity.TimeLineEvent;
import ezria.lifetrackr.Entity.TimelineEventType;
import ezria.lifetrackr.Handler.ChangeHandler;
import ezria.lifetrackr.Mapper.FocusMapper;
import ezria.lifetrackr.Mapper.ItemMapper;
import ezria.lifetrackr.Mapper.TimeLineEventMapper;
import ezria.lifetrackr.VO.TimeLineEventVO;
import ezria.lifetrackr.service.TimeLineEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TimeLineEventServiceImpl implements TimeLineEventService {

    @Autowired
    private TimeLineEventMapper timeLineEventMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private FocusMapper focusMapper;

    @Autowired
    private List<ChangeHandler> handlers;

    @Override
    public void saveItemEvent(ItemDTO itemDTO, Long itemId) {
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
    public Page<TimeLineEventVO> getTimeLineEvents(Long userId, Long itemId, String eventType,String itemType,
                                                    LocalDate startDate, LocalDate endDate,
                                                    Integer pageNum, Integer pageSize) {
        Page<TimeLineEvent> page = new Page<>(pageNum, pageSize);

        QueryWrapper<TimeLineEvent> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

        if (itemId != null) {
            wrapper.eq("item_id", itemId);
        }
        if (itemType != null && !itemType.isEmpty()) {
            // item_type 在 item 表，不在 timeline_event 表，先查匹配的 item_id
            QueryWrapper<Item> itemWrapper = new QueryWrapper<>();
            itemWrapper.eq("user_id", userId).eq("type", itemType).select("id");
            List<Object> itemIds = itemMapper.selectObjs(itemWrapper);
            if (itemIds.isEmpty()) {
                itemIds.add(-1L); // 无匹配项时传入不存在的 ID，保证返回空结果
            }
            wrapper.in("item_id", itemIds);
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

        // 批量查询关联的 Item，避免 N+1 问题
        Set<Long> itemIds = eventPage.getRecords().stream()
                .map(TimeLineEvent::getItemId)
                .collect(Collectors.toSet());
        Map<Long, Item> itemMap = Map.of();
        if (!itemIds.isEmpty()) {
            List<Item> items = itemMapper.selectBatchIds(itemIds);
            itemMap = items.stream().collect(Collectors.toMap(Item::getId, Function.identity()));
        }
        final Map<Long, Item> finalItemMap = itemMap;

        // 批量查询关联的 FocusSession
        Set<Long> focusIds = eventPage.getRecords().stream()
                .map(TimeLineEvent::getFocusSessionId)
                .filter(id -> id != null)
                .collect(Collectors.toSet());
        Map<Long, FocusSession> focusMap = Map.of();
        if (!focusIds.isEmpty()) {
            List<FocusSession> sessions = focusMapper.selectBatchIds(focusIds);
            focusMap = sessions.stream().collect(Collectors.toMap(FocusSession::getId, Function.identity()));
        }
        final Map<Long, FocusSession> finalFocusMap = focusMap;

        List<TimeLineEventVO> voList = eventPage.getRecords().stream()
                .map(event -> toVO(event,
                        event.getItemId() != null ? finalItemMap.get(event.getItemId()) : null,
                        event.getFocusSessionId() != null ? finalFocusMap.get(event.getFocusSessionId()) : null))
                .collect(Collectors.toList());

        Page<TimeLineEventVO> voPage = new Page<>(pageNum, pageSize, eventPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    public void saveFocusSessionEvent(FocusSession focusSession, Long focusSessionId) {
        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(focusSession.getUserId());
        event.setFocusSessionId(focusSessionId);
        event.setEventType(TimelineEventType.FOCUS_SESSION_START);
        event.setDescription(focusSession.getGoal());
        timeLineEventMapper.eventInsert(event);
        System.out.println(focusSessionId);
    }

    @Override
    public void completedFocusSessionEvent(FocusSession session) {
        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(session.getUserId());
        event.setFocusSessionId(session.getId());
        event.setEventType(TimelineEventType.FOCUS_SESSION_COMPLETE);
        event.setDescription(session.getGoal());
        timeLineEventMapper.eventInsert(event);
    }

    @Override
    public void cancelFocusSessionEvent(FocusSession session) {
        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(session.getUserId());
        event.setFocusSessionId(session.getId());
        event.setEventType(TimelineEventType.FOCUS_SESSION_CANCEL);
        event.setDescription(session.getGoal());
        timeLineEventMapper.eventInsert(event);
    }

    @Override
    public void saveLoginEvent(Integer id) {
        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(id.longValue());
        event.setEventType(TimelineEventType.LOGIN_SUCCESSFULLY);
        event.setDescription("用户登录");
        timeLineEventMapper.eventInsert(event);
    }


    private TimeLineEventVO toVO(TimeLineEvent event, Item item, FocusSession focusSession) {
        TimeLineEventVO vo = new TimeLineEventVO();
        BeanUtils.copyProperties(event, vo);
        vo.setEventType(event.getEventType().name());

        if (item != null) {
            vo.setItemTitle(item.getTitle());
            vo.setItemCover(item.getCover());
            vo.setItemRating(item.getRating());
            vo.setItemType(item.getType());
            vo.setItemStatus(item.getStatus());
        }
        if (focusSession != null) {
            vo.setFocusSessionStartTime(focusSession.getStartTime());
            vo.setFocusSessionGoal(focusSession.getGoal());
            vo.setFocusSessionDuration(focusSession.getActualDuration());
        }
        return vo;
    }
}

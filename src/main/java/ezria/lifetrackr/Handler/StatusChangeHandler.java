package ezria.lifetrackr.Handler;

import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Entity.TimeLineEvent;
import ezria.lifetrackr.Entity.TimelineEventType;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 检测状态变化：{@code status} 从 A 变为 B，
 * 并映射到对应事件类型（开始/完成/修改）。
 */
@Component
public class StatusChangeHandler implements ChangeHandler {

    @Override
    public TimeLineEvent handle(Item oldItem, ItemDTO newDTO) {
        if (newDTO.getStatus() == null || Objects.equals(oldItem.getStatus(), newDTO.getStatus())) {
            return null;
        }

        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(oldItem.getUserId());
        event.setItemId(oldItem.getId());
        event.setEventType(mapEventType(oldItem.getStatus(), newDTO.getStatus()));
        event.setDescription("状态: " + oldItem.getStatus() + " → " + newDTO.getStatus());
        event.setEventData("{\"status\":\"" + newDTO.getStatus() + "\"}");
        return event;
    }

    /**
     * 将状态变更映射为具体事件类型
     */
    private TimelineEventType mapEventType(String oldStatus, String newStatus) {
        if ("doing".equals(newStatus)) {
            return TimelineEventType.START_ITEM;
        }
        if ("done".equals(newStatus)) {
            return TimelineEventType.COMPLETE_ITEM;
        }
        return TimelineEventType.UPDATE_ITEM;
    }
}

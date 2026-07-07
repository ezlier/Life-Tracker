package ezria.lifetrackr.Handler;

import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Entity.TimeLineEvent;
import ezria.lifetrackr.Entity.TimelineEventType;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 检测评分变化：{@code rating} 从 A 变为 B。
 */
@Component
public class RatingChangeHandler implements ChangeHandler {

    @Override
    public TimeLineEvent handle(Item oldItem, ItemDTO newDTO) {
        // 前端没传 rating 或前后值相同 → 无变化
        if (newDTO.getRating() == null || Objects.equals(oldItem.getRating(), newDTO.getRating())) {
            return null;
        }

        TimeLineEvent event = new TimeLineEvent();
        event.setUserId(oldItem.getUserId());
        event.setItemId(oldItem.getId());
        event.setEventType(TimelineEventType.RATE_ITEM);
        event.setDescription("评分: " + oldItem.getRating() + " → " + newDTO.getRating());
        event.setEventData("{\"rating\":" + newDTO.getRating() + "}");
        return event;
    }
}

package ezria.lifetrackr.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineEventVO {
    // 事件字段
    private Long id;
    private Long userId;
    private Long itemId;
    private String eventType;
    private String description;
    private String eventData;
    private LocalDateTime createdAt;

    // 关联 Item 信息
    private String itemTitle;
    private String itemCover;
    private Integer itemRating;
    private String itemType;
    private String itemStatus;
}

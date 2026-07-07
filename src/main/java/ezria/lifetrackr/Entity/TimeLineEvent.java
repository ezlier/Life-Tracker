package ezria.lifetrackr.Entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("timeline_event")
public class TimeLineEvent {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 所属用户
     */
    private Long userId;

    /**
     * 对应的 Item
     */
    private Long itemId;

    /**
     * 事件类型
     */
    private TimelineEventType eventType;

    /**
     * 事件描述
     * 可为空
     */
    private String description;

    /**
     * 扩展数据(JSON)
     * <p>
     * 例如：
     * {"rating":9}
     * {"status":"done"}
     * {"commentId":15}
     */
    private String eventData;

    /**
     * 是否公开
     */
    private Boolean isPublic;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
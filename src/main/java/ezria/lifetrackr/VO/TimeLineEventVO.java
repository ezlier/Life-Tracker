package ezria.lifetrackr.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TimeLineEventVO {
    private Long id;
    private Long userId;
    private Long itemId;
    private String eventType;
    private String description;
    private String eventData;
    private LocalDateTime createdAt;
}

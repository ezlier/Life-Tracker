package ezria.lifetrackr.module.Admin.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminTimeLineEventVO {
    private Long id;
    private Long userId;
    private String userName;
    private Long itemId;
    private Long focusSessionId;
    private String eventType;
    private String description;
    private String eventData;
    private Boolean isPublic;
    private LocalDateTime createdAt;
}

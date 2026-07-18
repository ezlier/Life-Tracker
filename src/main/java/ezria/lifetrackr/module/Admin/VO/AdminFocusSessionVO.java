package ezria.lifetrackr.module.Admin.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminFocusSessionVO {
    private Long id;
    private Long userId;
    private String userName;
    private Long itemId;
    private String mode;
    private String goal;
    private Integer plannedDuration;
    private Integer actualDuration;
    private Integer pauseDuration;
    private String status;
    private Boolean isCompleted;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDateTime createTime;
}

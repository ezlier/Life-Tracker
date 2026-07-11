package ezria.lifetrackr.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FocusSession {
    private Long id;
    private Long userId;
    private Long itemId;   //可为空
    private String mode; // 番茄钟、倒计时、正计时等模式，便于扩展
    private String goal; // 用户专注的目标，便于扩展
    private Integer plannedDuration; // 用户计划专注多久
    private Integer actualDuration; // 实际专注多久，用于统计
    private Integer pauseDuration; // 用户暂停的总时长，用于统计
    private String status; // 进行中、已完成、已取消等状态，便于扩展
    private Boolean isCompleted;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

package ezria.lifetrackr.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FocusSessionDTO {
    Long id;
    Long userId;
    Long itemId;
    String mode;
    Integer plannedDuration;
    String goal;
    boolean isCompleted;
    LocalDateTime startTime;
    LocalDateTime endTime;
}

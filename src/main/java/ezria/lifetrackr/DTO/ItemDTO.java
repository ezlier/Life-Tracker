package ezria.lifetrackr.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDTO {
    Long id;
    Long userId;

    String type;      // movie/book/game/workout
    String title;     // 标题
    String cover;
    String description;

    Integer rating;   // 评分（1~10）
    String status;    // planned / doing / done
}

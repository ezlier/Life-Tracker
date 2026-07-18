package ezria.lifetrackr.module.Admin.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminItemVO {
    Long id;
    Long userId;
    String name;

    String type;      // movie/book/game/workout
    String title;     // 标题
    String cover;     // 封面图片
    String description;

    Integer rating;   // 评分（1~10）
    String status;    // planned / doing / done

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

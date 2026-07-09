package ezria.lifetrackr.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;




/**
 * Item entity representing a trackable item in the application.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    Long id;
    Long userId;

    String type;      // movie/book/game/workout
    String title;     // 标题
    String cover;

    String description;

    Integer rating;   // 评分（1~10）
    String status;    // planned / doing / done

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

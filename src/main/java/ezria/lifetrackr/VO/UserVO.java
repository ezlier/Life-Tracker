package ezria.lifetrackr.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
    private Long id;
    private String name;
    private String Email;
    private Integer phone;
    private String avatar;
    private String Introduction;
    private String isAdmin;
}

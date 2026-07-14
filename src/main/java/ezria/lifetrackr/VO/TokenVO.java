package ezria.lifetrackr.VO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenVO {
    private Long id;
    private String username;
    private String token;
}

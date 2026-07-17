package ezria.lifetrackr.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String newPwd;
    private String newPwd2;
    private String avatar;
    @JsonProperty("Introduction")
    private String Introduction;
    private Integer phone;
    @JsonProperty("Email")
    private String Email;
}

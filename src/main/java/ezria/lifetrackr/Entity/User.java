package ezria.lifetrackr.Entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;




/**
 * User entity class representing a user in the system.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String password;
    private String avatar;
    private String Introduction;
    private Integer phone;
    private String Email;
    private Boolean isAdmin;
}

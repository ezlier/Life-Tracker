package ezria.lifetrackr.Controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.VO.TokenVO;
import ezria.lifetrackr.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginService loginService;



    /**
     * 登录接口
     * @param userDTO User login information
     * @return Result JTWToken and user information if login is successful, error message otherwise
     */
    @PostMapping()
    public Result login(@RequestBody UserDTO userDTO) {
        log.info("Login attempt for user: {}", userDTO.getUsername());
        log.info("Password: {}", userDTO.getPassword());
        TokenVO tokenVO = loginService.login(userDTO);
        if (tokenVO != null) {
            return Result.success(tokenVO);
        }
        return Result.error("Login failed");
    }
}

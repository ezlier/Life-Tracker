package ezria.lifetrackr.Controller;


import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public Result getUser(@CurrentUserId Long userId) {
        log.info("Get user with id: {}", userId);

        return Result.success(userService.getUser(userId));
    }
}

package ezria.lifetrackr.module.Admin.Controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.module.Admin.Service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/Admin/User")
@RestController
public class AdminUserController {
    @Autowired
    private AdminUserService adminUserService;

    @GetMapping
    public Result GetUsers(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        log.info("GetUsers pageNum:{}, pageSize:{}", pageNum, pageSize);
        return Result.success(adminUserService.getUsersByPage(pageNum, pageSize));
    }

    @PatchMapping("/pwd")
    public Result resetPwd(@RequestBody UserDTO userDTO) {
        log.info("resetPwd id:{}", userDTO.getId());
        adminUserService.resetPassword(userDTO.getId());
        return Result.success();
    }

    @PatchMapping("/switch")
    public Result switchIsAdmin(@RequestBody UserDTO userDTO) {
        log.info("switchIsAdmin id:{}", userDTO.getId());
        adminUserService.switchIsAdmin(userDTO.getId());
        return Result.success();
    }

    @DeleteMapping("/{ids}")
    public Result DeleteUsers(@PathVariable List<Long> ids) {
        log.info("DeleteUsers ids:{}", ids);
        adminUserService.deleteUsers(ids);
        return Result.success();
    }
}

package ezria.lifetrackr.module.Admin.Controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.module.Admin.Service.AdminFocusSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/Admin/FocusSession")
@RestController
public class AdminFocusSessionController {
    @Autowired
    private AdminFocusSessionService service;

    @GetMapping
    public Result GetSessions(@RequestParam(defaultValue = "1") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(service.getByPage(pageNum, pageSize));
    }

    @DeleteMapping("/{ids}")
    public Result DeleteSessions(@PathVariable List<Long> ids) {
        service.deleteSessions(ids);
        return Result.success();
    }
}

package ezria.lifetrackr.module.Admin.Controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.module.Admin.Service.AdminTimeLineEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/Admin/TimeLineEvent")
@RestController
public class AdminTimeLineEventController {
    @Autowired
    private AdminTimeLineEventService service;

    @GetMapping
    public Result GetEvents(@RequestParam(defaultValue = "1") int pageNum,
                            @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(service.getByPage(pageNum, pageSize));
    }

    @DeleteMapping("/{ids}")
    public Result DeleteEvents(@PathVariable List<Long> ids) {
        service.deleteEvents(ids);
        return Result.success();
    }
}

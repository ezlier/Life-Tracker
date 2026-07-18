package ezria.lifetrackr.module.Admin.Controller;

import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.module.Admin.Service.AdminItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/Admin/Item")
@RestController
public class AdminItemController {
    @Autowired
    private AdminItemService adminItemService;

    @GetMapping
    public Result GetItems(@RequestParam(defaultValue = "1") int pageNum,
                           @RequestParam(defaultValue = "10") int pageSize) {
        return Result.success(adminItemService.getItemsByPage(pageNum,pageSize));
    }

    @DeleteMapping("/{ids}")
    public Result DeleteItemS(@PathVariable List<Long> ids) {
        adminItemService.deleteItems(ids);
        return Result.success();
    }
}

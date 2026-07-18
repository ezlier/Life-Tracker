package ezria.lifetrackr.Controller;

import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.DTO.BillDTO;
import ezria.lifetrackr.service.BillService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/bill")
public class BillController {
    @Autowired
    private BillService billService;

    @PostMapping("/upload")
    public Result BillImport(@CurrentUserId Long userid, MultipartFile file) {
        billService.BillImport(userid, file);
        return Result.success();
    }

    @PostMapping
    public Result CreateBill(@CurrentUserId Long userid, @RequestBody BillDTO billDTO){
        billService.CreateBill(userid, billDTO);
        return Result.success();
    }

    @GetMapping
    public Result GetBill(@CurrentUserId Long userid,
                          @RequestParam(defaultValue = "1") int pageNum,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String income){
        return Result.success(billService.GetBill(userid, pageNum, pageSize, income));
    }

    @DeleteMapping("/{id}")
    public Result DeleteBill(@CurrentUserId Long userid,
                             @PathVariable Long id){
        billService.DeleteBill(userid, id);
        return Result.success();
    }
}

package ezria.lifetrackr.Controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.VO.ItemVO;
import ezria.lifetrackr.service.ItemService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @GetMapping
    public Result getItems(HttpServletRequest request,
                           @RequestParam(required = false) String type,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                           @RequestParam(defaultValue = "1") Integer pageNum,
                           @RequestParam(defaultValue = "10") Integer pageSize) {

        Long userId = (Long) request.getAttribute("userId");
        log.info("Getting items: userId={}, type={}, startDate={}, endDate={}", userId, type, startDate, endDate);
        Page<ItemVO> page = itemService.getItems(userId, type, startDate, endDate, pageNum, pageSize);
        return Result.success(page);
    }

    @GetMapping({"/{itemId}"})
    public Result getItem(HttpServletRequest request,
            @PathVariable Long itemId) {

        Long userId = (Long) request.getAttribute("userId");
        log.info("Getting item: userId={}, itemId={}", userId, itemId);
        ItemVO item = itemService.getItem(userId, itemId);
        return Result.success(item);
    }


    @PostMapping
    public Result save(HttpServletRequest request,
            @RequestBody ItemDTO itemDTO) {

        Long userId = (Long) request.getAttribute("userId");
        itemDTO.setUserId(userId);
        log.info("Saving item: {}", itemDTO);
        itemService.save(itemDTO);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(HttpServletRequest request,
                         @RequestBody List<Long> itemIds) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("Deleting items: userId={}, itemIds={}", userId, itemIds);
        for (Long itemId : itemIds) {
            itemService.delete(userId, itemId);
        }
        return Result.success();
    }

    @PutMapping
    public Result update(HttpServletRequest request,
                         @RequestBody ItemDTO itemDTO) {
        Long userId = (Long) request.getAttribute("userId");
        itemDTO.setUserId(userId);
        log.info("Updating item: {}", itemDTO);
        itemService.update(itemDTO);
        return Result.success();
    }
}

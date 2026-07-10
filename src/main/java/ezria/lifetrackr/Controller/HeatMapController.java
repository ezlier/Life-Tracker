package ezria.lifetrackr.Controller;

import ezria.lifetrackr.Common.Annotation.CurrentUserId;
import ezria.lifetrackr.Common.Result;
import ezria.lifetrackr.service.HeatMapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/heatmap")
public class HeatMapController {

    @Autowired
    private HeatMapService heatMapService;

    @GetMapping("/{year}")
    public Result getHeatMap(@PathVariable Integer year, @CurrentUserId Long userId) {
        log.info("User {} requested heat map data for year {}", userId, year);
        return Result.success(heatMapService.getYearHeatMap(userId, year));
    }

    @GetMapping("/type")
    public Result getTypeChart(@CurrentUserId Long userId) {
        log.info("User {} requested type chart data", userId);
        return Result.success(heatMapService.getTypeChartData(userId));
    }

    @GetMapping("/status")
    public Result getStats(@CurrentUserId Long userId) {
        log.info("User {} requested stats data", userId);
        return Result.success(heatMapService.getStats(userId));
    }
}

package ezria.lifetrackr.service;

import ezria.lifetrackr.Entity.HeatMap;

import java.util.List;
import java.util.Map;

public interface HeatMapService {
    List<HeatMap> getYearHeatMap(Long userId, Integer year);

    Map<String, Long> getTypeChartData(Long userId);

    Object getStats(Long userId);

    Object getDuration(Long userId);

    Object getFocusStats(Long userId);
}

package ezria.lifetrackr.service;

import ezria.lifetrackr.Entity.HeatMap;

import java.util.List;

public interface HeatMapService {
    List<HeatMap> getYearHeatMap(Long userId, Integer year);
}

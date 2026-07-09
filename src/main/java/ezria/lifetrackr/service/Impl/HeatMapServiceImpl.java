package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.Entity.HeatMap;
import ezria.lifetrackr.Mapper.HeatMapMapper;
import ezria.lifetrackr.service.HeatMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HeatMapServiceImpl implements HeatMapService {

    @Autowired
    private HeatMapMapper heatMapMapper;

    @Override
    public List<HeatMap> getYearHeatMap(Long userId, Integer year) {
        // 1. 查询该年有记录的日期及条数
        List<HeatMap> dbResult = heatMapMapper.countByYear(userId, year);
        Map<LocalDate, Integer> dateCountMap = dbResult.stream()
                .collect(Collectors.toMap(HeatMap::getDate, HeatMap::getCount));

        // 2. 生成全年 365/366 天，补齐空缺为 0
        int totalDays = LocalDate.of(year, 12, 31).getDayOfYear();
        List<HeatMap> result = new ArrayList<>(totalDays);

        LocalDate cursor = LocalDate.of(year, 1, 1);
        for (int i = 0; i < totalDays; i++) {
            result.add(new HeatMap(cursor, dateCountMap.getOrDefault(cursor, 0)));
            cursor = cursor.plusDays(1);
        }
        return result;
    }
}

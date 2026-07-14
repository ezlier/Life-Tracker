package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ezria.lifetrackr.Entity.FocusSession;
import ezria.lifetrackr.Entity.HeatMap;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Mapper.FocusMapper;
import ezria.lifetrackr.Mapper.HeatMapMapper;
import ezria.lifetrackr.Mapper.ItemMapper;
import ezria.lifetrackr.service.HeatMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class HeatMapServiceImpl implements HeatMapService {

    @Autowired
    private HeatMapMapper heatMapMapper;

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private FocusMapper focusMapper;

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

    @Override
    public Map<String, Long> getTypeChartData(Long userId) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.select("type, COUNT(*) AS cnt");
        wrapper.groupBy("type");
        List<Map<String, Object>> rows = itemMapper.selectMaps(wrapper);

        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (String) row.get("type"),
                        row -> (Long) row.get("cnt"),
                        (a, b) -> a
                ));
    }

    @Override
    public Object getStats(Long userId) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.select("status, COUNT(*) AS cnt");
        wrapper.groupBy("status");
        List<Map<String, Object>> rows = itemMapper.selectMaps(wrapper);

        return rows.stream()
                .collect(Collectors.toMap(
                        row -> (String) row.get("status"),
                        row -> (Long) row.get("cnt"),
                        (a, b) -> a
                ));
    }

    @Override
    public Object getDuration(Long userId) {
        QueryWrapper<FocusSession> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.select("COALESCE(SUM(actual_duration), 0) AS totalActualDuration",
                       "COALESCE(SUM(pause_duration), 0) AS totalPauseDuration");
        return focusMapper.selectMaps(wrapper).get(0);
    }

    @Override
    public Object getFocusStats(Long userId) {
        QueryWrapper<FocusSession> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.select("status, COUNT(*) AS cnt");
        wrapper.groupBy("status");
        return focusMapper.selectMaps(wrapper);
    }
}

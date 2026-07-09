package ezria.lifetrackr.Mapper;

import ezria.lifetrackr.Entity.HeatMap;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HeatMapMapper {

    /**
     * 按日期统计指定用户某年的 timeline_event 条数
     */
    @Select("SELECT DATE(created_at) AS date, COUNT(*) AS count " +
            "FROM timeline_event " +
            "WHERE user_id = #{userId} AND YEAR(created_at) = #{year} " +
            "GROUP BY DATE(created_at) " +
            "ORDER BY date")
    List<HeatMap> countByYear(Long userId, Integer year);
}

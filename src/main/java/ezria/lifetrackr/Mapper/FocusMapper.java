package ezria.lifetrackr.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ezria.lifetrackr.Entity.FocusSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface FocusMapper extends BaseMapper<FocusSession> {
    int insertFocusSession(FocusSession focusSession);

    FocusSession selectById(Long id);

    @Select("SELECT * FROM focus_session WHERE id = #{id} AND user_id = #{userId}")
    FocusSession selectByIdAndUser(@Param("userId") Long userId, @Param("id") Long id);

    @Select("SELECT * FROM focus_session WHERE status = 'running'")
    List<FocusSession> selectRunningSessions();

    @Update("UPDATE focus_session " +
            "SET actual_duration = COALESCE(actual_duration, 0) + " +
            "TIMESTAMPDIFF(SECOND, start_time, NOW()), " +
            "status = 'paused', start_time = NOW() " +
            "WHERE id = #{id} AND user_id = #{userId} AND status = 'running'")
    int autoPauseIfRunning(@Param("userId") Long userId, @Param("id") Long id);

    int updateById(FocusSession session);
}

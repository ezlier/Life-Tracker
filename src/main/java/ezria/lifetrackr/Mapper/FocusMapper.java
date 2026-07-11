package ezria.lifetrackr.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ezria.lifetrackr.Entity.FocusSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FocusMapper extends BaseMapper<FocusSession> {
    int insertFocusSession(FocusSession focusSession);

    FocusSession selectById(Long id);

    int updateById(FocusSession session);
}

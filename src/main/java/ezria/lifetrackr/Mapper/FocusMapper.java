package ezria.lifetrackr.Mapper;

import ezria.lifetrackr.Entity.FocusSession;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FocusMapper {
    int insertFocusSession(FocusSession focusSession);

    FocusSession selectById(Long id);

    int updateById(FocusSession session);
}

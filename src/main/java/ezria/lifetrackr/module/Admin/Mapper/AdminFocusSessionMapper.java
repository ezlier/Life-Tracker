package ezria.lifetrackr.module.Admin.Mapper;

import ezria.lifetrackr.module.Admin.VO.AdminFocusSessionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AdminFocusSessionMapper {

    ArrayList<AdminFocusSessionVO> selectByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM focus_session")
    int countAll();

    void deleteByIds(@Param("ids") List<Long> ids);
}

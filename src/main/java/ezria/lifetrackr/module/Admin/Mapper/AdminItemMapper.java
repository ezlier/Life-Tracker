package ezria.lifetrackr.module.Admin.Mapper;

import ezria.lifetrackr.module.Admin.VO.AdminItemVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AdminItemMapper {
    ArrayList<AdminItemVO> selectItemByPage(@Param("offset") int offset, @Param("pageSize") int pageSize);

    @Select("SELECT COUNT(*) FROM item")
    int countItems();


    void deleteByIds(List<Long> ids);
}

package ezria.lifetrackr.Mapper;

import ezria.lifetrackr.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{userId}")
    User selectById(Long userId);
}

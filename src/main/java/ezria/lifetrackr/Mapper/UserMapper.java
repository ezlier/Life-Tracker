package ezria.lifetrackr.Mapper;

import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.Entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{userId}")
    User selectById(Long userId);

    int updateUser(@Param("userId") Long userId, @Param("dto") UserDTO dto);

    @Insert("INSERT INTO user (name, password, isAdmin)" +
            "VALUES (#{name}, #{password}, #{isAdmin})")
    void insert(User user);
}

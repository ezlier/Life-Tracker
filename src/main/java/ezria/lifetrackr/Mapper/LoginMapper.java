package ezria.lifetrackr.Mapper;


import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.Entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoginMapper {
    @Select("SELECT * FROM user WHERE name = #{username} AND password = #{password}")
    public User findUserByUsername(UserDTO userDTO);
}

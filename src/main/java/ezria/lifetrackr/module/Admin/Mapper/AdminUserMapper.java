package ezria.lifetrackr.module.Admin.Mapper;

import ezria.lifetrackr.Entity.User;
import ezria.lifetrackr.VO.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface AdminUserMapper {
    @Select("select * from user")
    ArrayList<UserVO> selectAllUser();

    @Select("select * from user LIMIT #{offset}, #{size}")
    ArrayList<UserVO> selectUsersByPage(@Param("offset") int offset, @Param("size") int size);

    @Select("SELECT COUNT(*) FROM user")
    int countUsers();

    @Delete("<script>" +
            "DELETE FROM user WHERE id IN " +
            "<foreach item='id' collection='ids' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    void deleteByIds(@Param("ids") List<Long> ids);

    @Update("UPDATE user SET password = #{password} WHERE id = #{id}")
    int updatePassword(Long id, String password);

    @Update("UPDATE user SET isAdmin = #{isAdmin} WHERE id = #{id}")
    int updateIsAdmin(@Param("id") Long id, @Param("isAdmin") Integer isAdmin);

    @Select("select * from user where ID = #{id}")
    User selectUserById(Long id);
}

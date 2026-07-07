package ezria.lifetrackr.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ezria.lifetrackr.Entity.Item;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {

    @Select("select * from item where user_id = #{userId} and id = #{itemId}")
    Item selectByIdAndUser(Long userId, Long itemId);

    @Insert("insert into item (user_id, type, title, description, rating, status) " +
            "values (#{userId}, #{type}, #{title}, #{description}, #{rating}, #{status})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void itemInsert(Item item);

    @Delete("delete from item where user_id = #{userId} and id = #{itemId}")
    void itemDelete(Long userId, Long itemId);

    void updateByIdAndUser(Item item);
}

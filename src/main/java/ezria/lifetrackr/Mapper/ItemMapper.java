package ezria.lifetrackr.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ezria.lifetrackr.Entity.Item;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ItemMapper extends BaseMapper<Item> {
    @Insert("insert into item (user_id, type, title, description, rating, status) " +
            "values (#{userId}, #{type}, #{title}, #{description}, #{rating}, #{status})")
    void itemInsert(Item item);

    @Delete("delete from item where user_id = #{userId} and id = #{itemId}")
    void itemDelete(Long userId, Long itemId);

    void updateByIdAndUser(Item item);
}

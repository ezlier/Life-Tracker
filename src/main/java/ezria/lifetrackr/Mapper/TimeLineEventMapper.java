package ezria.lifetrackr.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ezria.lifetrackr.Entity.TimeLineEvent;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface TimeLineEventMapper extends BaseMapper<TimeLineEvent> {

    @Insert("insert into timeline_event (user_id, item_id, event_type, description, event_data) " +
            "values (#{userId}, #{itemId}, #{eventType}, #{description}, #{eventData})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int eventInsert(TimeLineEvent event);
}

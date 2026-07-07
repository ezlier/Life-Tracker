package ezria.lifetrackr.Handler;

import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Entity.TimeLineEvent;

/**
 * 变更检测处理器接口。
 * 每个实现负责检测 Item 某一字段的变化，返回对应的 TimeLineEvent；无变化返回 null。
 */
public interface ChangeHandler {

    /**
     * 对比旧记录与新 DTO，生成时间线事件
     * @param oldItem 数据库中的旧记录
     * @param newDTO  前端传来的更新数据
     * @return 时间线事件，无变化则返回 null
     */
    TimeLineEvent handle(Item oldItem, ItemDTO newDTO);
}

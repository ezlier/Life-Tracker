package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Mapper.ItemMapper;
import ezria.lifetrackr.VO.ItemVO;
import ezria.lifetrackr.service.ItemService;
import ezria.lifetrackr.service.TimeLineEventService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Autowired
    private TimeLineEventService timeLineEventService;

    @Override
    public Page<ItemVO> getItems(Long userId, String type, LocalDate startDate, LocalDate endDate,
                                  Integer pageNum, Integer pageSize) {
        // 1. 构建分页对象
        Page<Item> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
        }
        if (startDate != null) {
            wrapper.ge("created_at", startDate.atStartOfDay());
        }
        if (endDate != null) {
            wrapper.le("created_at", endDate.plusDays(1).atStartOfDay());
        }
        wrapper.orderByDesc("created_at");

        // 3. 分页查询
        Page<Item> itemPage = itemMapper.selectPage(page, wrapper);

        // 4. 转换为 VO（Entity → VO，脱敏/裁剪字段）
        List<ItemVO> voList = itemPage.getRecords().stream()
                .map(this::toVO)
                .collect(Collectors.toList());

        // 5. 构建返回的分页对象
        Page<ItemVO> voPage = new Page<>(pageNum, pageSize, itemPage.getTotal());
        voPage.setRecords(voList);
        return voPage;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(ItemDTO itemDTO) {
        // 1. 插入 item
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        itemMapper.itemInsert(item);
        Long itemId = item.getId();

        // 2. 插入时间线事件（同一事务，任一失败则全部回滚）
        timeLineEventService.save(itemDTO, itemId);

        return itemId;
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemMapper.itemDelete(userId, itemId);
    }

    @Override
    public ItemVO getItem(Long userId, Long itemId) {
        Item item = itemMapper.selectByIdAndUser(userId, itemId);
        return toVO(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);

        timeLineEventService.createLog(itemDTO, item.getId());

        itemMapper.updateByIdAndUser(item);
    }

    private ItemVO toVO(Item item) {
        ItemVO vo = new ItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
}

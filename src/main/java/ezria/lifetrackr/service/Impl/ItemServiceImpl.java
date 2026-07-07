package ezria.lifetrackr.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.Entity.Item;
import ezria.lifetrackr.Mapper.ItemMapper;
import ezria.lifetrackr.VO.ItemVO;
import ezria.lifetrackr.service.ItemService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemMapper itemMapper;

    @Override
    public Page<ItemVO> getItems(Long userId, String type, Integer pageNum, Integer pageSize) {
        // 1. 构建分页对象
        Page<Item> page = new Page<>(pageNum, pageSize);

        // 2. 构建查询条件
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if (type != null && !type.isEmpty()) {
            wrapper.eq("type", type);
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
    public void save(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        itemMapper.itemInsert(item);
    }

    @Override
    public void delete(Long userId, Long itemId) {
        itemMapper.itemDelete(userId, itemId);
    }

    @Override
    public ItemVO getItem(Long userId, Long itemId) {
        QueryWrapper<Item> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("id", itemId);
        Item item = itemMapper.selectOne(wrapper);
        return toVO(item);
    }

    @Override
    public void update(ItemDTO itemDTO) {
        Item item = new Item();
        BeanUtils.copyProperties(itemDTO, item);
        itemMapper.updateByIdAndUser(item);
    }

    private ItemVO toVO(Item item) {
        ItemVO vo = new ItemVO();
        BeanUtils.copyProperties(item, vo);
        return vo;
    }
}

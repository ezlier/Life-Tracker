package ezria.lifetrackr.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ezria.lifetrackr.DTO.ItemDTO;
import ezria.lifetrackr.VO.ItemVO;

public interface ItemService {
    Page<ItemVO> getItems(Long userId, String type, Integer pageNum, Integer pageSize);

    Long save(ItemDTO itemDTO);

    void delete(Long userId, Long itemId);

    ItemVO getItem(Long userId, Long itemId);

    void update(ItemDTO itemDTO);
}

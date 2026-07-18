package ezria.lifetrackr.module.Admin.Service.Impl;

import ezria.lifetrackr.module.Admin.Mapper.AdminItemMapper;
import ezria.lifetrackr.module.Admin.Service.AdminItemService;
import ezria.lifetrackr.module.Admin.VO.AdminItemVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminItemServiceImpl implements AdminItemService {
    @Autowired
    private AdminItemMapper adminItemMapper;


    @Override
    public Map<String, Object> getItemsByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        ArrayList<AdminItemVO> list = adminItemMapper.selectItemByPage(offset, pageSize);
        int total = adminItemMapper.countItems();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", total);
        return result;
    }

    @Override
    public void deleteItems(List<Long> ids) {
        adminItemMapper.deleteByIds(ids);
    }
}

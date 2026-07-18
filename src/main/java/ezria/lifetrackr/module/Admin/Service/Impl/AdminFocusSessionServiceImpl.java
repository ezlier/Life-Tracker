package ezria.lifetrackr.module.Admin.Service.Impl;

import ezria.lifetrackr.module.Admin.Mapper.AdminFocusSessionMapper;
import ezria.lifetrackr.module.Admin.Service.AdminFocusSessionService;
import ezria.lifetrackr.module.Admin.VO.AdminFocusSessionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminFocusSessionServiceImpl implements AdminFocusSessionService {
    @Autowired
    private AdminFocusSessionMapper mapper;

    @Override
    public Map<String, Object> getByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        ArrayList<AdminFocusSessionVO> list = mapper.selectByPage(offset, pageSize);
        int total = mapper.countAll();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        result.put("total", total);
        return result;
    }

    @Override
    public void deleteSessions(List<Long> ids) {
        mapper.deleteByIds(ids);
    }
}

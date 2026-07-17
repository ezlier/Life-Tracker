package ezria.lifetrackr.module.Admin.Service.Impl;

import ezria.lifetrackr.Entity.User;
import ezria.lifetrackr.VO.UserVO;
import ezria.lifetrackr.module.Admin.Mapper.AdminUserMapper;
import ezria.lifetrackr.module.Admin.Service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Autowired
    private AdminUserMapper adminUserMapper;

    @Override
    public ArrayList<UserVO> getUsers() {
        return adminUserMapper.selectAllUser();
    }

    @Override
    public Map<String, Object> getUsersByPage(int pageNum, int pageSize) {
        int offset = (pageNum - 1) * pageSize;
        ArrayList<UserVO> list = adminUserMapper.selectUsersByPage(offset, pageSize);
        int total = adminUserMapper.countUsers();

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        result.put("pageNum", pageNum);
        result.put("pageSize", pageSize);
        return result;
    }

    @Override
    public void deleteUsers(List<Long> ids) {
        adminUserMapper.deleteByIds(ids);
    }

    @Override
    public int resetPassword(Long id) {
        return adminUserMapper.updatePassword(id, "123456");
    }

    @Override
    public int switchIsAdmin(Long id) {
        User user = adminUserMapper.selectUserById(id);
        if (user.getIsAdmin()){
            return adminUserMapper.updateIsAdmin(id,0);
        }
        return adminUserMapper.updateIsAdmin(id, 1);
    }
}

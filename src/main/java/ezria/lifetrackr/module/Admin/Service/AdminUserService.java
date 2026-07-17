package ezria.lifetrackr.module.Admin.Service;

import ezria.lifetrackr.VO.UserVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface AdminUserService {
    ArrayList<UserVO> getUsers();

    Map<String, Object> getUsersByPage(int pageNum, int pageSize);

    void deleteUsers(List<Long> ids);

    int resetPassword(Long id);

    int switchIsAdmin(Long id);
}

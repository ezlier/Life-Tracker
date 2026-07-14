package ezria.lifetrackr.service;

import ezria.lifetrackr.VO.UserVO;

public interface UserService {
    UserVO getUser(Long userId);
}

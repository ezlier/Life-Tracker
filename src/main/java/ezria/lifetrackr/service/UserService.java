package ezria.lifetrackr.service;

import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.VO.UserVO;

public interface UserService {
    UserVO getUser(Long userId);

    void updateUser(Long userId, UserDTO userDTO);

    void createUser(UserDTO userDTO);
}

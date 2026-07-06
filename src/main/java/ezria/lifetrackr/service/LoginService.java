package ezria.lifetrackr.service;


import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.VO.UserVO;
public interface LoginService {
    UserVO login(UserDTO userDTO);
}

package ezria.lifetrackr.service;


import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.VO.TokenVO;
public interface LoginService {
    TokenVO login(UserDTO userDTO);
}

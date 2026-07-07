package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.Common.Utils.JwtUtils;
import ezria.lifetrackr.DTO.UserDTO;

import ezria.lifetrackr.Entity.User;
import ezria.lifetrackr.Mapper.LoginMapper;
import ezria.lifetrackr.VO.UserVO;
import ezria.lifetrackr.service.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private JwtUtils jwtUtils;


    @Override
    public UserVO login(UserDTO userDTO) {
        User user = loginMapper.findUserByUsername(userDTO);
        log.info("User found: {}", user);
        if (user != null) {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getName());
            String token = jwtUtils.generateToken(userMap);
            return new UserVO(user.getId(), user.getName(), token);

        }
        return null;
    }
}

package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.DTO.UserDTO;
import ezria.lifetrackr.Entity.User;
import ezria.lifetrackr.Mapper.UserMapper;
import ezria.lifetrackr.VO.UserVO;
import ezria.lifetrackr.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public UserVO getUser(Long userId) {
        User user = userMapper.selectById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public void updateUser(Long userId, UserDTO userDTO) {
        // 密码更新：三个密码字段都传了才处理
        if (userDTO.getNewPwd() != null && !userDTO.getNewPwd().isEmpty()) {
            User user = userMapper.selectById(userId);
            if (user == null) {
                throw new RuntimeException("User not found");
            }
            if (userDTO.getPassword() == null || !user.getPassword().equals(userDTO.getPassword())) {
                throw new RuntimeException("Old password is incorrect");
            }
            if (!userDTO.getNewPwd().equals(userDTO.getNewPwd2())) {
                throw new RuntimeException("Two new passwords do not match");
            }
            // 校验通过，将新密码写入 dto，由 XML 动态 SQL 更新
            userDTO.setPassword(userDTO.getNewPwd());
        } else {
            // 不更新密码时清空，避免 XML 误写旧密码
            userDTO.setPassword(null);
        }
        userMapper.updateUser(userId, userDTO);
    }

    @Override
    public void createUser(UserDTO userDTO) {
        if (userDTO.getNewPwd().equals(userDTO.getNewPwd2())) {
            User user = new User();
            user.setName(userDTO.getUsername());
            user.setPassword(userDTO.getNewPwd());
            user.setIsAdmin(false);
            log.info("Creating new user: {}, pwd: {}", user.getName(), user.getPassword());
            userMapper.insert(user);
        } else {
            throw new RuntimeException("Two new passwords do not match");
        }
    }
}

package ezria.lifetrackr.service.Impl;

import ezria.lifetrackr.Entity.User;
import ezria.lifetrackr.Mapper.UserMapper;
import ezria.lifetrackr.VO.UserVO;
import ezria.lifetrackr.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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
}

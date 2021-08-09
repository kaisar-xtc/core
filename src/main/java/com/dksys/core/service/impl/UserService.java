package com.dksys.core.service.impl;

import com.dksys.core.entity.SysLogin;
import com.dksys.core.entity.User;
import com.dksys.core.mapper.UserMapper;
import com.dksys.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 13:45:00
 */
@Service
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User queryUserByusername(String username) {

        return userMapper.queryUserByusername(username);
    }
}

package com.dksys.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.dksys.core.entity.LoginUser;
import com.dksys.core.entity.User;
import com.dksys.core.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 13:42:00
 */
@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.queryUserByusername(username);

        if (StrUtil.isEmptyIfStr(user)) {
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        }

        return getUserDetail(user);
    }

    public UserDetails getUserDetail(User user) {
        return new LoginUser(user);
    }
}

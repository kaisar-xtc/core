package com.dksys.core.service;

import com.dksys.core.entity.SysLogin;
import com.dksys.core.entity.User;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 13:39:00
 */
public interface IUserService {

    /**
     * 通过用户名查询User对象
     *
     * @param username
     * @return
     */
    User queryUserByusername(String username);

}

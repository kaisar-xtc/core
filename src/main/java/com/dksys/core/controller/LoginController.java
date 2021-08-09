package com.dksys.core.controller;

import cn.hutool.core.util.StrUtil;
import com.dksys.core.commons.ReslutBean;
import com.dksys.core.entity.SysLogin;
import com.dksys.core.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 10:37:00
 */
@RestController
public class LoginController {
    @Autowired
    private LoginService loginService;

    @PostMapping("/login")
    public ReslutBean login(@RequestBody SysLogin sysLogin) {
        ReslutBean ajax = new ReslutBean();

        String token = loginService.login(sysLogin.getUsername(), sysLogin.getPassword());

        ajax.put("token", token);
        return ajax;

    }

    @GetMapping("/temp")
    public String temp() {
        return "temp";
    }
}

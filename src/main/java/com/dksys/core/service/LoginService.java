package com.dksys.core.service;

import com.dksys.core.commons.TokenService;
import com.dksys.core.entity.LoginUser;
import com.dksys.core.excption.CustomException;
import com.dksys.core.excption.UserPasswordNotMatchException;
import com.dksys.core.utils.RsaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 11:00:00
 */
@Component
public class LoginService {
    @Resource
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    public String login(String username, String password) {

        Authentication authenticate = null;

        try {

            //这里我么自定义账号验证 用于前后端密码传输加密
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, RsaUtil.decryptByPrivateKey(password));
            //该方法会触发UserDetailsService里的loadUserByUsername方法
            authenticate = authenticationManager.authenticate(authenticationToken);

        } catch (Exception e) {

            if (e instanceof BadCredentialsException) {

                throw new UserPasswordNotMatchException("用户名密码不正确");
            } else {

                throw new CustomException(e.getMessage());
            }

        }
        //获取到用户对象
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        // 生成token
        return tokenService.createToken(loginUser);
    }

}

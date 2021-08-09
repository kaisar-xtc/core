package com.dksys.core.config.handle;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dksys.core.commons.ReslutBean;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version 1.0.0
 * @Author WangZiHao
 * @createTime 2021-08-06 14:18:00
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        int code = 401;
        String message = StrUtil.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        String str = JSONUtil.parseObj(ReslutBean.error(401, message)).toString();
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        out.print(str);
        out.flush();
        out.close();
    }
}

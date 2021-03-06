package com.dksys.core.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.dksys.core.commons.ReslutBean;
import com.dksys.core.commons.TokenService;
import com.dksys.core.config.handle.AuthenticationEntryPointImpl;
import com.dksys.core.entity.LoginUser;
import com.dksys.core.filter.JwtAuthenticationTokenFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 14:07:00
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    TokenService tokenService;
    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;
    /**
     * token???????????????
     */
    @Autowired
    private JwtAuthenticationTokenFilter authenticationTokenFilter;

    /**
     * ???????????????????????????
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/login", "/verifyCode", "/temp");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //??????csrf??????????????????session
                .csrf().disable()
                // ?????????????????????
                .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                // ??????token??????????????????session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // ????????????
                .authorizeRequests()
                // ????????????login ?????????captchaImage ??????????????????
                .antMatchers("/login", "/captchaImage","/temp").anonymous()
                .antMatchers(
                        HttpMethod.GET,
                        "/*.html",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                )
                .permitAll()
                // ???????????????????????????????????????????????????
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().disable();

        httpSecurity.logout().logoutUrl("/logout").logoutSuccessHandler(new LogoutSuccessHandler() {
            @Override
            public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                LoginUser loginUser = tokenService.getLoginUser(request);

                if (StrUtil.isEmptyIfStr(loginUser))
                {
                    String userName = loginUser.getUsername();
                    // ????????????????????????
                    tokenService.delLoginUser(loginUser.getToken());
                }
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out = response.getWriter();
                out.write(JSONUtil.parseObj(ReslutBean.error(null, "????????????")).toString());
                out.flush();
                out.close();
            }
        });
        // ??????JWT filter
        httpSecurity.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

    }

    /**
     * ???????????????????????????
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }


    /**
     * ??????????????????
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * ?????? ?????????????????? AuthenticationManager
     *
     * @return
     * @throws Exception
     */
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }
}

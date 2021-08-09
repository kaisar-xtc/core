package com.dksys.core.commons;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.dksys.core.entity.LoginUser;
import com.dksys.core.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @version 1.0.0
 * @Author Wang
 * @createTime 2021-08-06 11:31:00
 */
@Component
public class TokenService {

    @Value("${jwt.header}")
    private String header;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expireTime}")
    private int expireTime;

    protected static final long MILLIS_SECOND = 1000;

    protected static final long MILLIS_MINUTE = 60 * MILLIS_SECOND;

    private static final Long MILLIS_MINUTE_TEN = 20 * 60 * 1000L;

    @Autowired
    private RedisCache redisCache;

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request)
    {
        // 获取请求携带的令牌
        String token = getToken(request);

        if (!StrUtil.isEmpty(token))
        {
            JWTPayload jwtPayload = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) jwtPayload.getClaim("login_user_key");
            String userKey = getTokenKey(uuid);
            LoginUser user = redisCache.getCacheObject(userKey);
            return user;
        }
        return null;
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token)
    {
        if (StrUtil.isNotEmpty(token))
        {
            String userKey = getTokenKey(token);
            redisCache.deleteObject(userKey);
        }
    }
    
    public JWTPayload parseToken(String token) {
        return JWT.of(token).getPayload();
    }
    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request)
    {
        String token = request.getHeader(header);
        if (StrUtil.isNotEmpty(token) && token.startsWith("Bearer "))
        {
            token = token.replace("Bearer ", "");
        }
        return token;
    }
    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser)
    {
        String token = UUID.fastUUID().toString();
        loginUser.setToken(token);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put("login_user_key",token);
        return createToken(claims);
    }

    /**
     * 使用java工具类Hutool生成JWT
     *
     * @param claims
     * @return
     */
    private String createToken(Map<String, Object> claims)
    {
        // SHA256withRSA
        String id = "rs256";
        JWTSigner signer = JWTSignerUtil.createSigner(id,
                // 随机生成密钥对，此处用户可自行读取`KeyPair`、公钥或私钥生成`JWTSigner`
                KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id)));

        String token = JWT.create().addPayloads(claims).setSigner(signer).sign();

        return token;
    }
    public void refreshToken(LoginUser loginUser) {

        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + expireTime * MILLIS_MINUTE);
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getToken());
        redisCache.setCacheObject(userKey, loginUser, expireTime, TimeUnit.MINUTES);
    }

    private String getTokenKey(String uuid)
    {
        return "login_tokens:" + uuid;
    }

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser)
    {
        long expireTime = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireTime - currentTime <= MILLIS_MINUTE_TEN)
        {
            refreshToken(loginUser);
        }
    }
}

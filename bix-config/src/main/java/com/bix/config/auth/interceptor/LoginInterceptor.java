package com.bix.config.auth.interceptor;

import com.bix.bixApi.common.exception.BixApiException;
import com.bix.bixApi.constant.RedisConstant;
import com.bix.bixApi.utils.RedisClient;
import com.bix.config.auth.AuthenticationManager;
import com.bix.config.auth.SecurityTemplate;
import com.bix.config.auth.UserInfo;
import com.bix.config.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@RequiredArgsConstructor
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    private final AuthenticationManager authenticationManager;
    private final SecurityTemplate securityTemplate;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedisClient redisClient;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        final Method method = ((HandlerMethod) handler).getMethod();

        //被UnAnth修饰的方法不验证登录
        if (method.isAnnotationPresent(UnAnth.class)) {
            return true;
        }
        String token = jwtUtil.getToken(request);
        if (token == null) {
            throw new BixApiException("无token信息");
        }
        if (!redisClient.exists(token)){
            throw new BixApiException("token已失效/过期");
        }
        //token里解析出来的 设备号
        String userAgentToken = jwtUtil.getUserAgent(token);
        String userAgent = request.getHeader("User-Agent");
        if (!jwtUtil.verifyToken(token) || !userAgentToken.equals(userAgent)) {
            throw new BixApiException("token无效");
        }
        this.checkAccountMuch(token, userAgent);
        return true;
    }
    private void checkAccountMuch(String token,String userAgent){
        String ssoMd5Token = DigestUtils.md5Hex(token.concat(userAgent));
        Long userId = jwtUtil.getUserId(token);
        String ssoLoginUidKey = "SSO_WALLET_ADMIN_".concat(userId.toString());
        String getMd5TokenByRedis = redisClient.get(ssoLoginUidKey);
        if(null == getMd5TokenByRedis || !getMd5TokenByRedis.equals(ssoMd5Token)){
            log.info("用户已被其他地方登录 userId=={}", userId);
            throw new BixApiException("用户已被修改密码或删除");
        }
    }
}

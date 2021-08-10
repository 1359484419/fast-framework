package com.bix.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationManager {

    @Value("${bixApi.auth-cookie-key}")
    private String authCookieKey;
    private final AuthenticationTemplate authenticationTemplate;

    /**
     * 解密cookie
     * @param cookies
     * @return
     */
    public UserInfo decrypt(Cookie[] cookies){
        if(cookies==null){
            return null;
        }
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(authCookieKey)){
                String token = cookie.getValue();
                log.debug("token值是："+token);
                return authenticationTemplate.getUserInfoForToken(token);
            }
        }
        return null;
    }



}

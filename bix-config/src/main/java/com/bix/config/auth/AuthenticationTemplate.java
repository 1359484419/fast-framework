package com.bix.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationTemplate {

    /**
     * 根据token获取登录用户
     * @param token
     * @return
     */
    @Cacheable(value = "userTokenCache")
    public UserInfo getUserInfoForToken(String token){
        return new UserInfo("1", "1" ,1l);
    }
}

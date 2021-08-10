package com.bix.config.auth;

import org.springframework.stereotype.Component;

@Component
public class SecurityTemplate {

    /**
     * 线程变量保存当前会话的用户，子线程也可以访问
     */
    private ThreadLocal<UserInfo> userInfoThreadLocal = new InheritableThreadLocal<UserInfo>(){
        @Override
        protected UserInfo initialValue() {
            return new UserInfo("system","system",1L);
        }
    };

    /**
     * 获取当前登录的用户
     * @return
     */
    public UserInfo getCurrentUser(){
        return userInfoThreadLocal.get();
    }

    /**
     * 设置当前登录用户
     * @param userInfo
     */
    public void setCurrentUser(UserInfo userInfo){
        this.userInfoThreadLocal.set(userInfo);
    }
}

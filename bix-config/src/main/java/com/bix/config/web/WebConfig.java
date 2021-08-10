package com.bix.config.web;

import com.bix.config.auth.AuthenticationManager;
import com.bix.config.auth.SecurityTemplate;
import com.bix.config.auth.interceptor.LoginInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * <p> </p>
 *
 * <pre> Created: 2019/5/20 13:24 </pre>
 *
 * @author  lx
 * @version 1.0
 * @since JDK 1.7
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final RequestJsonHandlerMethodArgumentResolver requestJsonHandlerMethodArgumentResolver;

    private final AuthenticationManager authenticationManager;
    private final SecurityTemplate securityTemplate;

    String excludeUrl = "zmx/**";
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(requestJsonHandlerMethodArgumentResolver);
        WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns("/user/login")
                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**");
    }

    @Bean
    public HandlerInterceptor userInterceptor() {
        return new LoginInterceptor(authenticationManager,securityTemplate);
    }
}

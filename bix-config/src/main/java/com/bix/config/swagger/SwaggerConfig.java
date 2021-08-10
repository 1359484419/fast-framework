package com.bix.config.swagger;

import com.bix.config.auth.interceptor.LoginInterceptor;
import com.fasterxml.classmate.TypeResolver;
import com.github.xiaoymin.swaggerbootstrapui.annotations.EnableSwaggerBootstrapUI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static springfox.documentation.schema.AlternateTypeRules.newRule;

/**
 * @author lx
 * @date 2020/10/9 13:53
 * @since 0.1.0
 */
@Configuration
@EnableSwagger2
@EnableSwaggerBootstrapUI
public class SwaggerConfig  extends WebMvcConfigurationSupport {
    @Autowired
    private TypeResolver typeResolver;

    @Resource
    private LoginInterceptor loginInterceptor;

    @Override

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/**").addResourceLocations(
                "classpath:/static/");
        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
// addPathPatterns 用于添加拦截规则 ， 先把所有路径都加入拦截， 再一个个排除
                .addPathPatterns("/**")
                .excludePathPatterns("/swagger-ui.html")
                .excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/webjars/**")
                .excludePathPatterns("/doc.html");

    }

    @Bean
    public Docket createRestApi() {
        List<Parameter> list = new ArrayList<Parameter>();
        ParameterBuilder token = new ParameterBuilder();
        token.name("Authorization").description("token，登陆成功后返回").modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build();
        list.add(token.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.bix.bixApi.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(list);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("-----------")
                .description("通用服务接口")
                .termsOfServiceUrl("http://localhost:8099/swagger-ui.html")
                .version("2.0")
                .build();
    }

}

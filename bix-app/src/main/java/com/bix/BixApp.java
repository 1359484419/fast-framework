package com.bix;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan(value = {"com.bix.bixApi.mapper"})
@EnableCaching
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class BixApp {
    public static void main( String[] args ) {
        SpringApplication.run(BixApp.class, args);
    }
}

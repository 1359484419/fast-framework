///*
// * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
// * Copyright (c) 2012-2018. haiyi Inc.
// * i2hub All rights reserved.
// */
//
//package com.bix.config.mybatisplus;
//
//import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
//import com.baomidou.mybatisplus.core.injector.ISqlInjector;
//import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.incrementer.IKeyGenerator;
//import com.baomidou.mybatisplus.incrementer.OracleKeyGenerator;
//import com.baomidou.mybatisplus.mapper.ISqlInjector;
//import com.baomidou.mybatisplus.mapper.LogicSqlInjector;
//import com.baomidou.mybatisplus.plugins.PaginationInterceptor;
//import com.baomidou.mybatisplus.plugins.PerformanceInterceptor;
//import com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusProperties;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.boot.context.properties.EnableConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Configuration
//@EnableConfigurationProperties(MybatisPlusProperties.class)
//@MapperScan("com.bix.**.mapper*")
//public class MybatisPlusConfig {
//
//
//    /**
//     * 分页插件，自动识别数据库类型
//     */
//    @Bean
//    public PaginationInterceptor paginationInterceptor() {
//        return new PaginationInterceptor();
//    }
//
//    /**
//     * 注入主键生成器
//     */
//    @Bean
//    public IKeyGenerator keyGenerator() {
//        return new OracleKeyGenerator();
//    }
//
//
//    /**
//     * 注入sql注入器
//     */
//    @Bean
//    public ISqlInjector sqlInjector() {
//        return new LogicSqlInjector();
//    }
//
//}

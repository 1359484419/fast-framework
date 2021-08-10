package com.bix.bixApi.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component("bixApi_springHodler")
public class SpringHolder implements ApplicationContextAware {


    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringHolder.applicationContext = applicationContext;
    }

    /**根据类型获取bean
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBean(Class<T> clazz){
        Assert.notNull(applicationContext,"applicationContext初始化失败");
        return applicationContext.getBean(clazz);
    }

    /**
     * 根据bean名获取bean
     * @param name
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T getBeanByName(String name,Class<T> clazz){
        Assert.notNull(applicationContext,"applicationContext初始化失败");
        return (T) applicationContext.getBean(name);
    }
}

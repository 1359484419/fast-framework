package com.bix.config.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p> 为解决json格式下以post方式接收参数必须要以对象形式接收，如果只接收一个参数就比较痛苦的问题。可以使用此注解。 </p>
 *
 * <pre> Created: 2019/5/20 13:15 </pre>
 *
 * @author  lx
 * @version 1.0
 * @since JDK 1.7
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
public @interface RequestJson {

    /**
     * 参数名，如果不写，就是参数名
     * @return
     */
    String value() default "";

    /**
     * 是否必填
     * @return
     */
    boolean isRequired() default false;
}

package com.calyee.chat.common.common.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @className: RedissonLock
 * @author: Calyee
 * @description: 分布式事务注解
 * @date: 2024/03/13 013 22:11
 * @version: 1.0
 */

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface RedissonLock {

    /**
     * key的前缀，默认取方法全限定名，可以自己指定
     */
    String prefixKey() default "";

    /**
     * 支持SpringEL表达式的key
     */
    String key() default "";

    /**
     * 等待锁的排队时间，默认不需要等待 快速失败
     */
    int waitTime() default -1;

    /**
     * 时间单位，默认毫秒
     */
    TimeUnit unit() default TimeUnit.MILLISECONDS;
}

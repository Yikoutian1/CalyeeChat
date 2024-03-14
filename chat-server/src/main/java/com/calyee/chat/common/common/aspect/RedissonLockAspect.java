package com.calyee.chat.common.common.aspect;

import com.calyee.chat.common.common.annotation.RedissonLock;
import com.calyee.chat.common.common.service.LockService;
import com.calyee.chat.common.common.utils.SpElUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @className: RedissonAspect
 * @author: Calyee
 * @description: RedissonAspect分布式锁切面
 * @date: 2024/03/13 013 22:17
 * @version: 1.0
 */
@Component
@Aspect
@Order(0) // 确保比事务的注解先执行，分布式锁在事务外，如果锁在事务内，那么锁是失效的
public class RedissonLockAspect {
    @Autowired
    private LockService lockService;

    @Around("@annotation(redissonLock)")
    public Object around(ProceedingJoinPoint joinPoint, RedissonLock redissonLock) throws Throwable {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String prefix = StringUtils.isBlank(redissonLock.prefixKey())
                ? SpElUtils.getMethodKey(method) : redissonLock.prefixKey();
        String key = SpElUtils.parseSpEl(method, joinPoint.getArgs(), redissonLock.key());
        return lockService.executeWithLock(prefix + key, redissonLock.waitTime(), redissonLock.unit(), () -> joinPoint.proceed());
    }
}

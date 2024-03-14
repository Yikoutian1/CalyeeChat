package com.calyee.chat.common.common.service;

import com.calyee.chat.common.common.exception.BusinessException;
import com.calyee.chat.common.common.exception.CommonErrorEnum;
import lombok.SneakyThrows;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @className: LockService
 * @author: Calyee
 * @description: 分布式锁Service
 * @date: 2024/03/13 013 21:28
 * @version: 1.0
 */

@Service
public class LockService {
    @Autowired
    private RedissonClient redissonClient;

    @SneakyThrows // 外部不关心异常
    public <T> T executeWithLock(String key, int waitTime, TimeUnit timeUnit, Supplier<T> supplier) { // Supplier：只有入参，没有出参
        RLock lock = redissonClient.getLock(key);
        boolean success = lock.tryLock(waitTime, timeUnit);
        if (!success) { // 失败
            throw new BusinessException(CommonErrorEnum.lOCK_LIMIT);
        }
        try {
            return supplier.get();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 不需要等待
     */
    @SneakyThrows
    public <T> T executeWithLock(String key, Supplier<T> supplier) { // Supplier：只有入参，没有出参
        return this.executeWithLock(key, -1, TimeUnit.MINUTES, supplier);
    }

    @SneakyThrows
    public <T> T executeWithLock(String key, Runnable runnable) { // Supplier：只有入参，没有出参
        return this.executeWithLock(key, -1, TimeUnit.MINUTES, () -> {
            runnable.run();
            return null;
        });
    }

    @FunctionalInterface
    public interface Supplier<T> {
        /**
         * Gets a result
         *
         * @return 啊 result
         */
        T get() throws Throwable;
    }
}

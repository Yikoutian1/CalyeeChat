package com.calyee.chat.common.common.thread;

import lombok.AllArgsConstructor;

import java.util.concurrent.ThreadFactory;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.thread
 * @className: MyThreadFactory
 * @author: Caluee
 * @description: 自定义线程工厂
 * @date: 2024/03/05 005 19:56
 * @version: 1.0
 */

@AllArgsConstructor
public class MyThreadFactory implements ThreadFactory {

    // 装饰器模式
    private static final MyUncaughtExceptionHandler myUncaughtExceptionHandler = new MyUncaughtExceptionHandler();

    private ThreadFactory original;

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = original.newThread(r); // 执行spring线程自己的创建逻辑
        // 额外装饰我们需要的创建逻辑
        thread.setUncaughtExceptionHandler(myUncaughtExceptionHandler);
        return thread;
    }
}

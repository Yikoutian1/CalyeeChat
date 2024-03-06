package com.calyee.chat.common.common.thread;

import lombok.extern.slf4j.Slf4j;

/**
 * @projectName: calyeechat
 * @package: com.calyee.chat.common.common.thread
 * @className: MyUncaughtExceptionHandler
 * @author: Caluee
 * @description: 自定义的异常处理类
 * @date: 2024/03/05 005 19:40
 * @version: 1.0
 */

@Slf4j
public class MyUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        log.error("Exception in thread", e);
    }
}

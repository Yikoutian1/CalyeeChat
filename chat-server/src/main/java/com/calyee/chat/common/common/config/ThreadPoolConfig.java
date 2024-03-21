package com.calyee.chat.common.common.config;

import com.calyee.chat.common.common.thread.MyThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer {
    /**
     * 项目共用线程池
     */
    public static final String CALYEECHAT_EXECUTOR = "calyeechatExecutor";

    /**
     * websocket通信线程池
     */
    public static final String WS_EXECUTOR = "websocketExecutor";

    @Override
    public Executor getAsyncExecutor() {
        return calyeechatExecutor();
    }

    @Bean(CALYEECHAT_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor calyeechatExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("calyeechat-executor-"); // 线程前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());//满了调用线程执行，认为重要任务
        executor.setThreadFactory(new MyThreadFactory(executor));// 设置线程工厂
        executor.initialize();
        return executor;
    }

    @Bean(WS_EXECUTOR)
    @Primary
    public ThreadPoolTaskExecutor websocketExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setCorePoolSize(16);
        executor.setMaxPoolSize(16);
        executor.setQueueCapacity(1000); // 1000个人推送
        executor.setThreadNamePrefix("calyeechat-executor-"); // 线程前缀
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());// 满了直接丢弃
        executor.setThreadFactory(new MyThreadFactory(executor));// 设置线程工厂
        executor.initialize();
        return executor;
    }

    // 如果其他的地方也需要拿到此线程池
//    @Autowired
//    @Qualifier(ThreadPoolConfig.CALYEECHAT_EXECUTOR)
//    private ThreadPoolTaskExecutor threadPoolTaskExecutor;
}
package com.zty.gateway.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池配置
 * @author ZhangTianYou
 */
@Configuration
@EnableAsync
public class ThreadPoolConfig implements AsyncConfigurer{

    @Bean
    @Primary
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor=new ThreadPoolTaskExecutor();
        //核心线程数
        executor.setCorePoolSize(20);
        //最大线程数
        executor.setMaxPoolSize(30);
        //阻塞队列容量,当线程全部在使用中时,后面的的任务会进入到阻塞队列中
        executor.setQueueCapacity(50);
        //线程存活时间
        executor.setKeepAliveSeconds(60);
        //当前线程池中的线程前缀名
        executor.setThreadNamePrefix("sync-");
        //等待所有任务结束后再关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //设置线程超时时间,防止线程阻塞
        executor.setAwaitTerminationSeconds(60);
        //定义拒绝策略,触发条件：当最大线程数与阻塞队列全部被占满了时就会触发
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池,初始化 core 核心线程
        executor.initialize();

        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }
}

package com.accessManagementSystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);          // minimum threads alive
        executor.setMaxPoolSize(500);          // can scale up to 500 threads dynamically
        executor.setQueueCapacity(1000);       // waiting queue capacity
        executor.setKeepAliveSeconds(60);      // free idle threads after 1 minute
        executor.setThreadNamePrefix("AMS-Worker-");
        executor.setAllowCoreThreadTimeOut(true); // allow core threads to expire when idle

        executor.initialize();
        return executor;
    }
}

package com.example.websocketdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
public class ThreadPoolConf {
    @Bean(value = "threadPoolInstance")
    public ThreadPoolExecutor createThreadPoolExecutor(){
        ThreadFactory threadFactory=new CustomizableThreadFactory("myThreadPool");
        ThreadPoolExecutor executor=
                new ThreadPoolExecutor(5, 10, 3, TimeUnit.MINUTES,new ArrayBlockingQueue<>(30),threadFactory,new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}

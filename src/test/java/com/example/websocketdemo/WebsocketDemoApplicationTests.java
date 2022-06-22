package com.example.websocketdemo;

import com.example.websocketdemo.threadpool.MyThreadPool;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;

import java.util.concurrent.*;

@SpringBootTest
class WebsocketDemoApplicationTests {
    @Autowired
    ThreadPoolExecutor threadPoolInstance;
    @Test
    void contextLoads() throws ExecutionException, InterruptedException {
        threadPoolInstance.execute(()->{
            System.out.println("fuck");
        });
        String future= threadPoolInstance.submit(() -> {
            return "123";
        }).get();
        System.out.println(future);
    }
    @Test
    void test0(){
        MyThreadPool myThreadPool = new MyThreadPool(2, 3);
        myThreadPool.execute(()->{
            for (int i=0;i<30;i++){

                System.out.println(Thread.currentThread()+"---------");
            }
        }) ;
        myThreadPool.execute(()->{
            for (int i=0;i<30;i++){

                System.out.println(Thread.currentThread()+"+++++++++++");
            }
        }) ;
        myThreadPool.execute(()->{
            for (int i=0;i<30;i++){
                System.out.println(Thread.currentThread()+"============");
            }
        }) ;
        myThreadPool.execute(()->{
            for (int i=0;i<30;i++){
                System.out.println(Thread.currentThread()+"*************");
            }
        }) ;


    }

}

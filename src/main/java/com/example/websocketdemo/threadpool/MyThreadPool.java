package com.example.websocketdemo.threadpool;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;


public class MyThreadPool {
//手写
    private int maxThreadNum;
    private BlockingQueue<Runnable> workQueue;
    public Set<WorkThread> workThreads;

    public MyThreadPool(int coreThreadNum,int maxThreadNum){
        this.maxThreadNum=maxThreadNum;
        this.workQueue=new ArrayBlockingQueue<>(maxThreadNum);
        this.workThreads=new HashSet<>();
        for (Integer i=0;i<coreThreadNum;i++){
            WorkThread workThread = new WorkThread("thread  " + i.toString());
            workThreads.add(workThread);
            workThread.start();
        }
    }

    public void execute(Runnable task){
        if(workQueue.size()<maxThreadNum){
            workQueue.add(task);
        }else {
            System.out.println("队列已满");
        }

    }

    public void destroy(){
        for (WorkThread workThread:workThreads){
            workThread=null;
        }
        workThreads.clear();
        workQueue.clear();
    }

    class WorkThread extends Thread{
        public WorkThread(String name){
            setName(name);
        }
        @Override
        public void run() {
            while (!isInterrupted()){
                try {
                    Runnable task = workQueue.take();
                    if (task!=null){
                        System.out.println(this.getName()+"正在执行");
                        task.run();
                    }
                    task=null;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


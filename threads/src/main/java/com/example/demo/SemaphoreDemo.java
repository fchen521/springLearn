package com.example.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 信号量
 *Semaphore是一个计数信号量，常用于限制可以访问某些资源（物理或逻辑的）线程数目。
 */
public class SemaphoreDemo {
    //（请求总数）
    public static int clientTotal = 10;

    // （同时并发执行的线程数）
    public static int permits = 2;


    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newCachedThreadPool();
        //permits就是允许同时运行的线程数目
        final Semaphore semaphore = new Semaphore(permits);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            final int count = i;
            executorService.execute(() -> {
                try {
                    //从信号量中获取一个许可
                    semaphore.acquire();
                    System.out.println("工作中");
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    //释放一个许可(在释放许可之前，必须先获获得许可。)
                    semaphore.release();
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executorService.shutdown();
        System.out.println("所有人都已经处理完...");
    }
}

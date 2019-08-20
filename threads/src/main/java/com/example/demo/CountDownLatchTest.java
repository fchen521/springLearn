package com.example.demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 使用时await如何不指定时间会一直等
 * 线程计数
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatch latch =new CountDownLatch(6);
        ExecutorService service = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            service.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName()+"开始运动。。。");
                    latch.countDown();
                }
            });
        }

        try {
            latch.await(10, TimeUnit.SECONDS);
            System.out.println("所有的都运动完毕");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

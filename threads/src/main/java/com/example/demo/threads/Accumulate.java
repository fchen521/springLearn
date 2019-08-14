package com.example.demo.threads;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 数据保持一致性的同时进行累加
 */
public class Accumulate {
    public static void main(String[] args) throws Exception {
        //单线程实现1到100累加操作
        //oneThread(); //大约需要10s

        //多线程实现1到100累加操作
        manyThread(); //大约需要0.1s

    }

    public static void oneThread() throws InterruptedException {
        long start = System.currentTimeMillis();
        Integer count = 100;
        Integer sum = 0;
        for (int a = 0; a < count; a++) {
            sum += a;
            //假设需要耗时的操作
            Thread.sleep(100);

        }
        System.out.println(String.format("结果：%d\n耗时：%d", sum, System.currentTimeMillis() - start));
    }

    public static void manyThread() throws InterruptedException {
        long start = System.currentTimeMillis();
        Integer count = 100;
        CountDownLatch countDownLatch = new CountDownLatch(count);
        AtomicInteger sum = new AtomicInteger(0);
        for (int a = 0; a < count; a++) {
            final Integer e = a;
            new Thread(
                    ()->{
                        sum .getAndAdd(e);
                        try {
                            //假设一次需要100毫秒
                            Thread.sleep(100);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                        countDownLatch.countDown();
                    }
            ).start();
        }
        countDownLatch.await();
        System.out.println(String.format("结果：%d\n耗时：%d", sum.get(), System.currentTimeMillis() - start));
    }
}

package com.example.demo.threads.locks;

import lombok.Data;

import java.util.Random;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Data
public class ReadWriteLockTest {
    private  int number = 0;

   private ReadWriteLock lock = new ReentrantReadWriteLock();

    public void read(){
        lock.readLock().lock();
        try {
            System.out.println("当前的值为"  + this.number);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void set(Integer value){
        lock.writeLock().lock();
        try {
            System.out.println(String.format("当前线程{%s}正在进行写操作{%s}", Thread.currentThread().getName(),value));
            this.setNumber(value);
        } finally {
            lock.writeLock().unlock();
        }

    }


    public static void main(String[] args) {
        ReadWriteLockTest demo = new ReadWriteLockTest();
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                demo.set(new Random().nextInt(101));
            },"write:" + (i+1)).start();
        }


        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                demo.read();
            }).start();
        }
    }

}

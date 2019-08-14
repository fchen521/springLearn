package com.example.demo.threads.locks;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    public static void main(String[] args)  {
        final Test test = new Test();

        new Thread(){
            public void run() {
                test.insert2(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.insert2(Thread.currentThread());
            };
        }.start();
    }

    public void insert(Thread thread) {
        lock.lock();
        try {
            System.out.println(thread.getName()+"得到了锁");
            for(int i=0;i<5;i++) {
                arrayList.add(i);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }finally {
            System.out.println(thread.getName()+"释放了锁");
            lock.unlock();
        }
    }

    public void insert2(Thread thread) {
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i=0;i<5;i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"获取锁失败");
        }
    }
}

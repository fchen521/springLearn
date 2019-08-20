package com.example.demo.threads.synchronizeds;

/**
 * 同步方法和同步块：
   同步方法就是在方法前加关键字synchronized，然后被同步的方法一次只能有一个线程进入，其他线程等待。
   而同步块则是在方法内部使用大括号使得一个代码块得到同步。同步块会有一个同步的”目标“，使得同步块更加灵活一些
  （同步块可以通过”目标“决定需要锁定的对象）。一般情况下，如果此”目标“为this，那么同步方法和同步块没有太大的区别。
 */
public class SyncTest {
    public static void main(String[] args) {

        HasSelfPrivateNum numRef = new HasSelfPrivateNum();
        HasSelfPrivateNum numRef2 = new HasSelfPrivateNum();
        ThreadA athread = new ThreadA(numRef);
        athread.start();

        ThreadB bthread = new ThreadB(numRef2);
        bthread.start();

    }
}
class HasSelfPrivateNum {

    private int num = 0;

    public void addI(String username) {
        try {
            if (username.equals("a")) {
                num = 100;
                System.out.println("a set over!");
                Thread.sleep(2000);
            } else {
                num = 200;
                System.out.println("b set over!");
            }
            System.out.println(username + " num=" + num);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}


class ThreadA extends Thread {

    private HasSelfPrivateNum numRef;

    public ThreadA(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("a");
    }

}



class ThreadB extends Thread {

    private HasSelfPrivateNum numRef;

    public ThreadB(HasSelfPrivateNum numRef) {
        super();
        this.numRef = numRef;
    }

    @Override
    public void run() {
        super.run();
        numRef.addI("b");
    }

}
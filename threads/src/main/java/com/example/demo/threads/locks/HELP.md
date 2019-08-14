```
public interface Lock {
    void lock();
    void lockInterruptibly() throws InterruptedException;
    boolean tryLock();
    boolean tryLock(long time, TimeUnit unit) throws InterruptedException;
    void unlock();
    Condition newCondition();
}

Lock锁接口方法详解

lock() 获得锁

lockInterruptibly() 获取锁定，除非当前线程是 interrupted 
lockInterruptibly()方法比较特殊，当通过这个方法去获取锁时，如果线程正在等待获取锁，则这个线程能够响应中断，即中断线程的等待状态。也就使说，当两个线程同时通过lock.lockInterruptibly()想获取某个锁时，假若此时线程A获取到了锁，而线程B只有在等待，那么对线程B调用threadB.interrupt()方法能够中断线程B的等待过程。

newCondition() 返回一个新Condition绑定到该实例Lock实例。

tryLock() 只有在调用时才可以获得锁。

tryLock(long time, TimeUnit unit) 如果在给定的等待时间内是空闲的，并且当前的线程尚未得到 interrupted，则获取该锁。

unlock()释放锁。
```

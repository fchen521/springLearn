import java.util.concurrent.locks.Lock;

public class ThreadSysTest implements Runnable {
    private int i;

    @Override
    public void run() {
       // synchronized (this) {
            i++;
            System.out.println(Thread.currentThread().getName() + "\t" + i);
        //}
    }
}
class Tmain{
    public static void main(String[] args) {

        for (int i = 0; i < 10; i++) {
            ThreadSysTest threadSysTest = new ThreadSysTest();
            new Thread(threadSysTest,"thread"+i).start();
        }
    }
}
import java.util.concurrent.CyclicBarrier;

public class ThreadTest {
    static final int COUNT = 5;
    static CyclicBarrier cb = new CyclicBarrier(COUNT, new Singer());

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < COUNT; i++) {
            new Thread(new Staff(i, cb)).start();
        }
        synchronized (ThreadTest.class) {
            ThreadTest.class.wait();
        }
    }

    static class Singer implements Runnable {

        @Override
        public void run() {
            System.out.println(String.format("为大家表演节目。。。"));
        }

    }

    static class Staff implements Runnable {

        CyclicBarrier cb;
        int num;

        Staff(int num, CyclicBarrier cb) {
            this.num = num;
            this.cb = cb;
        }

        @Override
        public void run() {
            System.out.println(String.format("员工(%d)出发。。。",num));
            System.out.println(String.format("员工(%d)到达地点一。。。", num));
            try {
                cb.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("员工(%d)出发。。。",num));
            System.out.println(String.format("员工(%d)到达地点2。。。", num));
            try {
                cb.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("员工(%d)出发。。。",num));
            System.out.println(String.format("员工(%d)到达地点3。。。", num));
            try {
                cb.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println(String.format("员工(%d)结束。。。", num));
        }

    }
}

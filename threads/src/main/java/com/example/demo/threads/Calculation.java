package com.example.demo.threads;

/**
 * 多线程计算1到1000000000之间的数字累加
 */
public class Calculation {
    private long startTime = 0L;
    private long endTime = 0L;
    private long totalResult = 0L;

    private Boolean[] isCompleted = null;

    private boolean isSuccessed(){
        for (int i = 0; i < isCompleted.length; i++) {
            if(!isCompleted[i])
                return false;
        }
        return true;
    }

    private void startUp(long numbers,int threadNum){
        System.out.println("开启的线程数为："+threadNum);
        isCompleted = new Boolean[threadNum];
        startTime = System.currentTimeMillis();
        for (int i = 1; i <= threadNum; i++) {
            isCompleted[i-1] = false;
            Thread t = new Thread(new CalcThread(i,numbers,threadNum));
            t.start();
        }
        synchronized (Calculation.this){
            try {
                Calculation.this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        endTime = System.currentTimeMillis();

        System.out.println("计算结果为："+totalResult);
        System.out.println("计时结束，总耗时为："+(endTime-startTime)+"ms");

    }

    @SuppressWarnings("Duplicates")
    class CalcThread implements Runnable{
        private long start;
        private long end;
        private long result;
        private int threadIndex;

        public CalcThread(int threadIndex,long numbers,long threadNum){
            long step = numbers/threadNum;
            this.threadIndex =  threadIndex;
            start = (threadIndex-1)*step+1;
            if (threadIndex==threadNum){
                end = numbers;
            }else {
                end = threadIndex*step;
            }
        }

        @Override
        public void run() {
            for (long i = start; i <=end ; i++) {
                result +=i;
            }
            synchronized (Calculation.this){
                totalResult +=result;
                isCompleted[this.threadIndex-1]=true;
                if (isSuccessed()){
                    Calculation.this.notify();
                }
            }
        }
    }


    public static void main(String[] args) {
        long numbers = 1000000000;
        new Calculation().startUp(numbers,100);
    }
}


package com.example.demo.threads;

import java.util.concurrent.RecursiveTask;
import java.util.stream.LongStream;

/**
 * ForkJoin 把一个大任务拆分成小任务，然后在汇总
 */
public class RecursiveTest {
    private static long startTime = 0L;
    private static long endTime = 0L;
    //定义最小区间
    private final  static  int MAX_THRESHOLD = 5000000;

    public static void main(String[] args) {
        /*final ForkJoinPool pool = new ForkJoinPool();
        startTime = System.currentTimeMillis();
        ForkJoinTask<Long> task = pool.submit(new CalculateRecursiveTask(1L,1000000000L));
        try {
            Long integer = task.get();
            System.out.println(integer);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        endTime = System.currentTimeMillis();
        System.out.println(endTime - startTime);*/
    }

    private static class CalculateRecursiveTask extends RecursiveTask<Long>{

        private Long start;

        private Long end;

        public CalculateRecursiveTask(Long start, Long end){
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if ((end - start) <= MAX_THRESHOLD){
                return LongStream.rangeClosed(start,end).sum();
            } else {
                Long middle = (start + end) / 2;
                CalculateRecursiveTask left = new CalculateRecursiveTask(start, middle);
                CalculateRecursiveTask right = new CalculateRecursiveTask(middle + 1, end);
                // 执行子任务
                //left.fork();
                //right.fork();
                invokeAll(left,right);
                return  left.join() + right.join();
            }
        }
    }
}

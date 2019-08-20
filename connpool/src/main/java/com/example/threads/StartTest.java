package com.example.threads;

public class StartTest {
    public static void main(String[] args) {
        if (args == null || args.length==0) {
            System.out.println("请输入执行ETL任务参数！");
            System.exit(0);
        }
        System.out.println("执行任务！"+args[0]);
    }
}

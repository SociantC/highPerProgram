package com.study.multithread.capture1;

public class Test2 {
    public static void main(String[] args) {
        Runnable task = ()->{
            int count = 0;
            for (; ; ) {
                System.out.println("------>1 "+count++);
            }
        };

        Runnable task2 = ()->{
            int count=0;
            for (; ; ) {
//                Thread.yield();
                System.out.println("          ------>2 " + count++);
            }
        };

        Thread t1 = new Thread(task, "t1");
        Thread t2 = new Thread(task2, "t2");
        t1.setPriority(Thread.MIN_PRIORITY);
        t2.setPriority(Thread.MAX_PRIORITY);
        t1.start();
        t2.start();

    }
}

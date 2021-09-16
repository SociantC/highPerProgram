package com.study.multithread.capture1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/9/16
 */
public class ThreadTest {
    public static class MyThread extends Thread{
        @Override
        public void run() {
            System.out.println("child thread");
        }
    }

    public static class RunnableTask implements Runnable{
        @Override
        public void run() {
            System.out.println("child thread");
        }
    }

    public static class CallerTask implements Callable<String>{
        @Override
        public String call() throws Exception {
            return "hello";
        }
    }

    public static void main(String[] args) {
//        MyThread myThread = new MyThread();
//        myThread.start();

//        RunnableTask task = new RunnableTask();
//        new Thread(task).start();
//        new Thread(task).start();

        FutureTask<String> futureTask = new FutureTask<>(new CallerTask());
        new Thread(futureTask).start();

        try {
            String result = futureTask.get();
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
}

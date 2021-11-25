package com.study.multithread.capture1;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class FutureTaskDemo {

    public static void main(String[] args) {

        FutureTask<Integer> futureTask = new FutureTask<>(() ->
        {
            System.out.println("running");
            Thread.sleep(1000);
            return 1;
        });

        int s;

        Thread thread = new Thread(futureTask,"t1");
        thread.start();

        System.out.println("main");

        System.out.println();
    }

}

package com.study.multithread.capture1;

import java.util.concurrent.TimeUnit;

public class Sleeper {
    public static void sleep (int num) {
        try {
            TimeUnit.SECONDS.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.TestCorrectPostureStep1")
public class TestCorrectPostureStep1 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;

    public static void main(String[] args) {
        new Thread(() -> {
            synchronized (room) {
                log.debug("yan??[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("no,wait");
                    sleep(2);
                }
                log.debug("yan??[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("start work");
                }
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                synchronized (room) {
                    log.debug("start work");

                }
            },"其他人").start();
        }

        sleep(1);


    }


    public static void sleep (int num) {
        try {
            TimeUnit.SECONDS.sleep(num);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

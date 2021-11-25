package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Tests")
public class TestFrames {
    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running");
//                System.out.println("running...");
            }
        };

        t1.run();
        t1.start();
    }
}

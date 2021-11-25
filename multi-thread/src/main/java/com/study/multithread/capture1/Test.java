package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.Test")
public class Test {
    static int i=0;
    static Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
//        int i=0;
        Thread t1 = new Thread(() -> {
            for (int j=0;j<10000;j++){
                synchronized (lock) {
                    i++;
                }
            }

            log.debug("i result:{}",i);
        },"t1");

        Thread t2 = new Thread(()->{
            for (int j=0;j<10000;j++){
                synchronized (lock){
                    i--;
                }

            }
            log.debug("i result:{}",i);
        },"t2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("final:{}",i);
    }
}

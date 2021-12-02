package com.study.multithread.capture2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.Test1")
public class Test1 {
    private static ReentrantLock lock = new ReentrantLock();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            if (!lock.tryLock()) {   // 尝试获得锁
                log.debug("没有获得锁");
                return;
            }
            try {
                log.debug("获得锁");
            }finally {
                lock.unlock();
            }
        },"t1");
        lock.lock();
        log.debug("获得锁");
        t1.start();
    }
}

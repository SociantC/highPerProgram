package com.study.multithread.capture2;

import com.study.multithread.capture1.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

@Slf4j(topic = "c.ReentranctLockCondition")
public class ReentranctLockCondition {
    static ReentrantLock lock = new ReentrantLock();
    static boolean cigarette = false;
    static boolean takeout = false;
    static Condition waitCigaretteSet = lock.newCondition();
    static Condition waitTakeoutSet = lock.newCondition();

    public static void main(String[] args) {

        new Thread(()->{
            lock.lock();
            while (!cigarette) {
                try {
                    log.debug("没有烟，不干活");
                    waitCigaretteSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            log.debug("开始干活了");
        },"等烟的").start();
        new Thread(()->{
            lock.lock();
            while (!takeout) {
                try {
                    log.debug("没有外卖，不干活");
                    waitTakeoutSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            log.debug("开始干活了");
        },"等外卖的").start();

        Sleeper.sleep(1);
        new Thread(()->{
            // 要先获得锁
            lock.lock();
            try {
                takeout = true;
                log.debug("外卖到了");
                waitTakeoutSet.signal();
            }finally {
                lock.unlock();
            }
        },"送外卖的").start();

        new Thread(()->{
            lock.lock();
            try {
                cigarette = true;
                log.debug("烟到了");
                waitCigaretteSet.signal();
            }finally {
                lock.unlock();
            }
        },"送烟的").start();
    }


}

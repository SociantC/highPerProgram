package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j(topic = "c.Test7")
public class Test7 {

    public static void main(String[] args) {
        GuardedObject2 guardedObject = new GuardedObject2();
        new Thread(()->{
            log.debug("等待下载");
            Object o = guardedObject.get(2000);
            log.debug("下载完成，内容{}",o);
        },"t1").start();

        new Thread(()->{
            try {
                log.debug("开始下载");
                Sleeper.sleep(1);
                guardedObject.set(null);
            } catch (Exception e) {
                e.printStackTrace();
            }

        },"t2").start();
    }

}


class GuardedObject2 {

    private Object object;

    public Object get(long timeout) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            long passed = 0;
            while (object == null) {
                // 本轮循环应该等待的时间
                long waitTime = timeout - passed;
                if (waitTime <= 0) {
                    break;
                }
                try {
                    this.wait(waitTime);   // 虚假唤醒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                passed = System.currentTimeMillis() - now;
            }
            return object;
        }
    }

    public void set(Object object) {
        synchronized (this) {
            this.object = object;
            this.notifyAll();
        }
    }
}

package com.study.lock.aqs.jucdemo.cdl;

import com.study.lock.aqs.AQSdemo;

import java.util.concurrent.atomic.AtomicInteger;

// CountDownLatch 自己实现
public class CDLdemo {
    AQSdemo aqSdemo = new AQSdemo() {
        @Override
        public int tryAcquireShared() { // 如果非等于0，代表当前还有线程没准备就绪，则认为需要等待
            return this.getState().get() == 0 ? 1 : -1;
        }

        @Override
        public boolean tryReleaseShared() { // 如果非等于0，代表当前还有线程没准备就绪，则不会通知继续执行
            return this.getState().decrementAndGet() == 0;
        }
    };

    public CDLdemo(int count) {
        aqSdemo.setState(new AtomicInteger(count));
    }

    public void await() {
        aqSdemo.acquireShared();
    }

    public void countDown() {
        aqSdemo.releaseShared();
    }

    public static void main(String[] args) throws InterruptedException {
        CDLdemo cdLdemo = new CDLdemo(10);
        for (int i = 0; i < 9; i++) { // 启动九个线程，最后一个两秒后启动
            new Thread(() -> {
                System.out.println(Thread.currentThread() + ".准备就绪");
                cdLdemo.countDown();
                cdLdemo.await();
                System.out.println("我是" + Thread.currentThread() + ".我执行了");
            }).start();
        }
        // 等待2秒，最后的线程才启动
        Thread.sleep(2000L);
        new Thread(() -> {
            cdLdemo.countDown();
            cdLdemo.await();
            System.out.println("我是最后一个线程.我来召唤神龙");
        }).start();
    }
}

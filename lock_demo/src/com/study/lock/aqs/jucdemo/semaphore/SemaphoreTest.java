package com.study.lock.aqs.jucdemo.semaphore;

import java.util.Random;
import java.util.concurrent.Semaphore;

// 信号量机制
public class SemaphoreTest {
    public static void main(String[] args) {
        SemaphoreTest semaphoreTest = new SemaphoreTest();
        int N = 8;            // 客人数量
        Semaphore semaphore = new Semaphore(5); // 手牌数量
        for (int i = 0; i < N; i++) {
            String vipNo = "vip-00" + i;
            new Thread(() -> {
                try {
                    semaphore.acquire(); // 获取令牌

                    semaphoreTest.service(vipNo);

                    semaphore.release(); // 释放令牌
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        }
    }

    public void service(String vipNo) throws InterruptedException {
        System.out.println("楼上出来迎接贵宾一位，贵宾编号" + vipNo + "，...");
        Thread.sleep(new Random().nextInt(3000));
        System.out.println("欢送贵宾出门，贵宾编号" + vipNo);
    }

}
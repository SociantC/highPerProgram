package com.study.multithread.capture2;

public class TestOrder1 {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{
            waitNotify.print(1,2);
        }).start();
        new Thread(()->{
            waitNotify.print(2,3);
        }).start();
        new Thread(()->{
            waitNotify.print(3,1);
        }).start();
    }
}

class WaitNotify{
    private int flag;
    private int loopNumber;

    public void print(int waitFlag, int nextFlag){
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this){
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(waitFlag);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
        System.out.println();
    }
}

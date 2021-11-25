package com.study.multithread.capture1.exercise;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;
/*
for /L %n in (1,1,10) do java -cp com.study.multithread.capture1.exercise.ExerciseShell
 */
// 卖票练习
public class ExerciseShell {
    public static void main(String[] args) {
        TickerWindow tickerWindow = new TickerWindow(1000);
        List<Thread> list = new ArrayList<>();


        List<Integer> sellCount = new Vector<>();
        for (int i = 0; i < 20000; i++) {
            Thread thread = new Thread(()->{
                int count = tickerWindow.sail(randomCount());
                try {
                    Thread.sleep(randomCount());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sellCount.add(count);
            });

            list.add(thread);
            thread.start();

        }

        list.forEach((t)->{
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        System.out.println("余票：{}"+tickerWindow.getNum());
        System.out.println("卖出：{}"+sellCount.stream().mapToInt(i->i).sum());

    }

    static Random random = new Random();

    public static int randomCount(){
        return random.nextInt(5) + 1;
    }

}


class TickerWindow{
    private int num;

    public TickerWindow(int num) {
        this.num = num;

    }

    public  int getNum() {
        return num;
    }

    public synchronized int sail(int sailNum) {
        if (sailNum <= num) {
            num -= sailNum;
            return sailNum;
        }else {
            return 0;
        }
    }
}
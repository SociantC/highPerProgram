package com.study.multithread.capture2;

import com.study.multithread.capture1.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class TestDeadLock {
    public static void main(String[] args) {
        Chopsticks c1 = new Chopsticks("1");
        Chopsticks c2 = new Chopsticks("2");
        Chopsticks c3 = new Chopsticks("3");
        Chopsticks c4 = new Chopsticks("4");
        Chopsticks c5 = new Chopsticks("5");

        new Philospher("a",c1,c2).start();
        new Philospher("b",c2,c3).start();
        new Philospher("c",c3,c4).start();
        new Philospher("d",c4,c5).start();
        new Philospher("e",c5,c1).start();

    }
}

@Slf4j(topic = "c.Philospher")
class Philospher extends Thread{
    Chopsticks left;
    Chopsticks right;

    public Philospher(String name, Chopsticks left, Chopsticks right) {
        super(name);
        this.left = left;
        this.right = right;
    }



    @Override
    public void run() {
        while (true) {
            if (left.tryLock()){
                try {
                    if (right.tryLock()){
                        try {
                            eat();
                        }finally {
                            right.unlock();
                        }
                    }

                }finally {
                    left.unlock();
                }
            }
        }
    }

    Random random = new Random();

    private void eat(){
        log.debug("eat");
        Sleeper.sleep(random.nextInt(1));
    }
}


class Chopsticks extends ReentrantLock {
    String name;

    public Chopsticks(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Chopsticks{" +
                "name='" + name + '\'' +
                '}';
    }
}

package com.study.multithread.design.singleton;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private AtomicInteger id = new AtomicInteger(0);
    private static final IdGenerator instance = new IdGenerator();

    private IdGenerator() {

    }

    public static IdGenerator getInstance() {
        return instance;
    }

    public long getId(){
        return id.getAndIncrement();
    }
}

package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

@Slf4j(topic = "c.Test6")
public class Test6 {

    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();

        new Thread(() -> {
            log.debug("get result");
            List<String> o = (List<String>) guardedObject.get();
            for (String s : o) {
                log.debug(s);
            }
        }, "t2").start();

        new Thread(() -> {
            log.debug("start");
            try {
                guardedObject.set(UrlDownload.download());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "t1").start();



    }

}

class GuardedObject{
    private Object object;


    public Object get(){
        synchronized (this) {
            while (object == null) {
                try {
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return object;
        }
    }

    public void set(Object o){
        synchronized (this) {
            this.object = o;
            this.notifyAll();
        }
    }

}

package com.study.multithread.capture1;

import javafx.geometry.Pos;
import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.Test8")
public class Test8 {

    public static void main(String[] args) {
        for (int i = 0; i < 3; i++) {
            new People().start();
        }
        Sleeper.sleep(1);
        for (Integer id : MailBoxes.getKey()) {
            new Postman(id, "msg" + id).start();
        }
    }

}

class MailBoxes{
    private static Map<Integer,GuardedObject3> boxes = new Hashtable<>();

    private static int id = 1;

    public static synchronized int generateId(){
        return id++;
    }

    public static GuardedObject3 createBox(){
        GuardedObject3 guardedObject3 = new GuardedObject3(generateId());
        boxes.put(guardedObject3.getId(), guardedObject3);
        return guardedObject3;
    }

    public static Set<Integer> getKey(){
        return boxes.keySet();
    }

    public static GuardedObject3 getBox(int id){
        return boxes.remove(id);
    }
}
@Slf4j(topic = "c.People")
class People extends Thread{
    @Override
    public void run() {
        GuardedObject3 guardedObject3 = MailBoxes.createBox();
        log.debug("开始收件,id:{}",guardedObject3.getId());
        Object o = guardedObject3.get(5000);
        log.debug("完成接收,id:{},content:{}",guardedObject3.getId(),o);
//        super.run();
    }
}
@Slf4j(topic = "c.Postman")
class Postman extends Thread{
    private int id;
    private String msg;

    public Postman(int id, String msg){
        this.id = id;
        this.msg = msg;
    }

    @Override
    public void run() {
        GuardedObject3 guardedObject3 = MailBoxes.getBox(id);
        guardedObject3.set(msg);
        log.debug("完成配送，id:{},msg:{}",guardedObject3.getId(),msg);
//        super.run();
    }
}

class GuardedObject3 {
    private int id;

    private Object object;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GuardedObject3(int id) {
        this.id = id;
    }

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

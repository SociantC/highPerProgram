package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

@Slf4j(topic = "c.ProducerConsumerTest")
public class ProducerConsumerTest {

    public static void main(String[] args) {
        MessageQueue queue = new MessageQueue(2);
        for (int i = 0; i < 3; i++) {
            int id = i;
            new Thread(()->{
                queue.enqueue(new Message(id,"消息"+id));
            },"生产者"+id).start();
        }
        new Thread(()->{
            while (true) {
                Sleeper.sleep(1);
                Message message = queue.take();
            }
        },"消费者").start();
    }

}


@Slf4j(topic = "c.MessageQueue")
class MessageQueue{
    private LinkedList<Message> queue = new LinkedList<>();

    private int capacity;

    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }
    // 拿出消息，队首
    public Message take(){
        synchronized (queue) {

            while (queue.isEmpty()) {
                log.debug("队列为空，消费者等待");
                try {
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Message message = queue.removeFirst();
            // 通知放入，有空间了
            log.debug("消费者消费消息，id:{},msg:{}", message.getId(), message.getMsg());
            queue.notifyAll();
            return message;
        }
    }
    // 放入消息，队尾
    public void enqueue(Message message){
        synchronized (queue) {
            while (queue.size() == capacity) {
                try {
                    log.debug("队列已满，生产者等待");
                    queue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            queue.addLast(message);
            log.debug("生产者生产消息,id:{},msg:{}", message.getId(), message.getMsg());
            // 通知拿出
            queue.notifyAll();
        }
    }
}


final class Message{
    private int id;
    private String msg;


    public int getId() {
        return id;
    }

    public String getMsg() {
        return msg;
    }

    public Message(int id, String msg) {
        this.id = id;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", msg='" + msg + '\'' +
                '}';
    }
}

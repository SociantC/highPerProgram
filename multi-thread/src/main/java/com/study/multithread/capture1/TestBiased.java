package com.study.multithread.capture1;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

@Slf4j(topic = "c.TestBiased")
public class TestBiased {

    public static void main(String[] args) {
        Dog dog = new Dog();
        System.out.println(ClassLayout.parseInstance(dog).toPrintable(true));
    }
}

class Dog{

}

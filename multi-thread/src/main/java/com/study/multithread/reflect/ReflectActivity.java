package com.study.multithread.reflect;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2021/10/15
 */
@Slf4j(topic = "c.Reflect")
public class ReflectActivity{
    private void getClassObject(){
        Class<?> classObject = null;
        classObject = getClassObject_1();
        log.debug("classObject_1 name:{}", classObject.getName());

        classObject = getClassObject_2();
        log.debug("classObject_2 name:{}", classObject.getName());

        classObject = getClassObject_3();
        log.debug("classObject_3 name:{}", classObject.getName());
    }

    private void getAllMethods() {
        ProgramMonkey programMonkey = new ProgramMonkey("xiaoming", "male", 12);
        Method[] methods = programMonkey.getClass().getMethods();

        for (Method method : methods) {
            log.debug("method name:{}", method.getName());
        }

        try {
            Method setMLanguageMethod = programMonkey.getClass().getMethod("setmLanguage", String.class);
            setMLanguageMethod.setAccessible(true);

            // 获取返回类型
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getClassObject_1(){
        return ProgramMonkey.class;
    }

    private Class<?> getClassObject_2(){
        ProgramMonkey programMonkey = new ProgramMonkey("xiaoming", "male", 12);
        return programMonkey.getClass();
    }

    private Class<?> getClassObject_3(){
        try {
            return Class.forName("com.study.multithread.reflect.ProgramMonkey");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}

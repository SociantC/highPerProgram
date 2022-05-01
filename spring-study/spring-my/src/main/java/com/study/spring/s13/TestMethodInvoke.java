package com.study.spring.s13;

import java.lang.reflect.Method;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class TestMethodInvoke {
    public static void main(String[] args) throws Exception {


        Method foo = TestMethodInvoke.class.getDeclaredMethod("foo", int.class);
        for (int i = 0; i < 17; i++) {
            show(i, foo);
            foo.invoke(null, i);
        }
        System.in.read();
    }
    // MethodAccessor
    private static void show(int i, Method foo) throws Exception{
        System.out.println(i + ":" + foo.getDeclaringClass().toString());
    }

    public static void foo(int i){
        System.out.println(i + ":foo");
    }
}

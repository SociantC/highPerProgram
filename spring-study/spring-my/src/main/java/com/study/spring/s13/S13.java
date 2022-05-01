package com.study.spring.s13;

import com.study.spring.s12.JdkProxyDemo;

import java.lang.reflect.Method;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class S13 {
    interface Foo{
        void foo();

        int bar();
    }

    static class Target implements Foo {
        @Override
        public void foo() {
            System.out.println("target foo");
        }

        @Override
        public int bar() {
            System.out.println("target bar");
            return 100;
        }
    }

//    interface InvocationHandler{
//        /**
//         *
//         * @param method 方法对象
//         * @param args 参数
//         * @return
//         * @throws Throwable
//         */
//        Object invoke(Object proxy,Method method, Object[] args) throws Throwable;
//    }

    public static void main(String[] args) {
        Foo proxy = new $Proxy0((proxy1,method,args1) -> {
            // 功能增强
            System.out.println("before");

            // 调用
//            new Target().foo();
            return method.invoke(new Target(), args1);
        });
        proxy.foo();
        int bar = proxy.bar();
        System.out.println(bar);
        /*

         */
    }
}

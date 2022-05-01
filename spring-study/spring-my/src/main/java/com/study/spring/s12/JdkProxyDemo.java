package com.study.spring.s12;

import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 说明: jdk代理实际上是直接生成字节码进行的代理，不经过源码、编译阶段 ASM 运行期间动态生成字节码
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class JdkProxyDemo {
    interface Foo{
        void foo();
    }

    static class Target implements Foo{
        @Override
        public void foo() {
            System.out.println("target foo");
        }
    }

    public static void main(String[] args) throws IOException {
        Target target = new Target();

        ClassLoader loader = JdkProxyDemo.class.getClassLoader();
        Foo proxy = (Foo) Proxy.newProxyInstance(loader, new Class[]{Foo.class}, (p, method, args1) -> {
            System.out.println("before");

            Object result = method.invoke(target, args1);

            System.out.println("after");
            return result;
        });
        System.out.println(proxy.getClass());
        proxy.foo();

        System.in.read();

    }
}

package com.study.spring.s12;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class CglibProxyDemo {
    static class Target{
        public void foo(){
            System.out.println("target foo");

        }
    }
    // 代理类是子类型，目标类是父类型
    public static void main(String[] args) {
        Target target = new Target();
        Target proxy = (Target) Enhancer.create(Target.class, (MethodInterceptor) (p, method, objects, methodProxy) -> {
            System.out.println("before..");
//            method.invoke(target, objects); // 方法反射调用目标
            // methodProxy 避免反射调用
//            Object result = methodProxy.invoke(target, objects);    // 内部没有使用反射,需要目标进行调用，spring使用方式
            Object result = methodProxy.invokeSuper(p, args); // 只需要自己
            System.out.println("after");

            return result;
        });

        proxy.foo();
    }
}

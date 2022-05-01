package com.study.spring.s14;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.cglib.reflect.FastClass;

import java.lang.reflect.Method;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class S14 {
    public static void main(String[] args) {
        Target target = new Target();
        Proxy proxy = new Proxy((p, method, objects, methodProxy) -> {
            System.out.println("before");
//            method.invoke(target, objects); // 反射调用方式
            // FastClass cglib内部代理类
//            methodProxy.invoke(target, objects);    // 内部无反射，结合目标使用
            return methodProxy.invokeSuper(p,objects);  // 内部无反射，结合代理使用
        });

        proxy.save();
        proxy.save(1);
        proxy.save(2L);

    }
}

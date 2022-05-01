package com.study.spring.s14;


import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class Proxy extends Target {

    private MethodInterceptor methodInterceptor;

    public Proxy(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    public Proxy() {
    }

    static Method save0;
    static Method save2;
    static Method save1;
    static MethodProxy save0Proxy;
    static MethodProxy save1Proxy;
    static MethodProxy save2Proxy;

    static {
        try {
            save0 = Target.class.getMethod("save");
            save1 = Target.class.getMethod("save",int.class);
            save2 = Target.class.getMethod("save",long.class);
            // ()V ()代表无参，V代表返回值为void 字节码中描述参数信息的方式
            // 目标类型 代理类型 参数和返回值说明 代理方法 增强方法
            save0Proxy = MethodProxy.create(Target.class, Proxy.class, "()V", "save","saveSuper");
            save1Proxy = MethodProxy.create(Target.class, Proxy.class, "(I)V", "save","saveSuper");
            // long -> J
            save2Proxy = MethodProxy.create(Target.class, Proxy.class, "(J)V", "save","saveSuper");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }

    }


    // 带原始功能方法
    public void saveSuper(){
        super.save();
    }
    // 带原始功能方法
    public void saveSuper(int i){
        super.save(i);
    }
    // 带原始功能方法
    public void saveSuper(long i){
        super.save(i);
    }


    // 待增强功能方法
    @Override
    public void save() {
        try {
            methodInterceptor.intercept(this, save0, new Object[0], save0Proxy);
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
    // 待增强功能方法
    @Override
    public void save(int i) {
        try {
            methodInterceptor.intercept(this, save1, new Object[]{i}, save1Proxy);
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }
    // 待增强功能方法
    @Override
    public void save(long i) {
        try {
            methodInterceptor.intercept(this, save2, new Object[]{i}, save2Proxy);
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }



}

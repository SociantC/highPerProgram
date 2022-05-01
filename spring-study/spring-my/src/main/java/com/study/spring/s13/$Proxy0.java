package com.study.spring.s13;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;

/**
 * 说明: 声明代理类
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class $Proxy0 extends Proxy implements S13.Foo{


    public $Proxy0(InvocationHandler handler) {
        super(handler);
    }

    @Override
    public void foo() {
        try {
            h.invoke(this,foo,new Object[0]);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }

    @Override
    public int bar() {
        try {
            Object result = h.invoke(this, bar, new Object[0]);
            return (int) result;
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable throwable) {
            throw new UndeclaredThrowableException(throwable);
        }
    }


    static Method foo;
    static Method bar;

    static {
        // 方法对象
        try {
            foo = S13.Foo.class.getMethod("foo");
            bar = S13.Foo.class.getMethod("bar");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }
}

package org.springframework.aop.framework;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2022/5/2
 *
 * @author LSWXHS-CGH
 */
public class S18_around {
    public static void main(String[] args) throws Throwable {
        Target target = new Target();
        List<MethodInterceptor> methodInterceptorList = new ArrayList<>();
        methodInterceptorList.add(new Advice1());
        methodInterceptorList.add(new Advice2());

        MyInvocation invocation = new MyInvocation(target, Target.class.getMethod("foo"), new Object[0], methodInterceptorList);
        invocation.proceed();
    }

    static class Target{
        public void foo() {
            System.out.println("target foo");
        }
    }

    static class Advice1 implements MethodInterceptor{
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("advice1 before");
            Object result = invocation.proceed();// 调用下一个通知，如果没有直接调用目标(实际递归环节)
            System.out.println("advice1 after");
            return result;
        }
    }

    static class Advice2 implements MethodInterceptor{
        @Override
        public Object invoke(MethodInvocation invocation) throws Throwable {
            System.out.println("Advice2 before");
            Object result = invocation.proceed();// 调用下一个通知，如果没有直接调用目标(实际递归环节)
            System.out.println("Advice2 after");
            return result;
        }
    }

    static class MyInvocation implements MethodInvocation{

        private Object target;
        private Method method;
        private Object[] args;
        List<MethodInterceptor> methodInterceptorList;
        private int count = 1;  // 调用次数

        public MyInvocation(Object target, Method method, Object[] args, List<MethodInterceptor> methodInterceptorList) {
            this.target = target;
            this.method = method;
            this.args = args;
            this.methodInterceptorList = methodInterceptorList;
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public Object[] getArguments() {
            return args;
        }

        /**
         * 调用每一个环绕通知，调用目标
         * @return 带调用的对象
         * @throws Throwable
         */
        @Override
        public Object proceed() throws Throwable {
            // 设置调用次数
            if (count > methodInterceptorList.size()) {
                // 调用目标，返回并结束递归
                return method.invoke(target, args);
            }
            // 逐一调用
            MethodInterceptor interceptor = methodInterceptorList.get(count++ - 1);
            // 调用完毕
            return interceptor.invoke(this);
        }

        @Override
        public Object getThis() {
            return target;
        }

        @Override
        public AccessibleObject getStaticPart() {
            return method;
        }
    }
}

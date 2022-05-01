package com.study.spring.s15;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class S15 {
    @Aspect
    static class MyAspect{
        @Before("execution(* foo())")
        public void before(){
            System.out.println("前置增强");
        }

        @After("execution(* foo())")
        public void after(){
            System.out.println("后置增强");
        }
    }

    public static void main(String[] args) {
        /*
        两个切面概念：
        1. aspect =
                advice通知1 + pointCut切点1
                advice通知2 + pointCut切点2
                advice通知3 + pointCut切点3

        2. advisor = 更细粒度的切面，包含一个通知和切点
         */


        // 1.切点
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* foo())");
        // 2.通知
        MethodInterceptor advice = new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                System.out.println("before");
                Object proceed = invocation.proceed();
                System.out.println("after");
                return proceed;
            }
        };
        // 3.切面
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(pointcut, advice);
        // 4.代理
        /*
        proxyTargetClass = false 目标实现了接口，用 JDK 来生成代理
                                 目标没有实现接口，用 cglib 实现
        proxyTargetClass = true  总是使用 cglib 实现
         */
        Target2 target = new Target2();
        ProxyFactory factory = new ProxyFactory();
        factory.setTarget(target);
        factory.addAdvisor(advisor);    // 绑定切面
        factory.setInterfaces(target.getClass().getInterfaces());
        factory.setProxyTargetClass(false);
        Target2 proxy = ((Target2) factory.getProxy());
        System.out.println(proxy.getClass());

        proxy.foo();
        proxy.bar();

    }

    interface I1{
        void foo();
        void bar();
    }

    static class Target1 implements I1{
        @Override
        public void foo() {
            System.out.println("target1 foo");
        }

        @Override
        public void bar() {
            System.out.println("target1 bar");
        }
    }

    static class Target2{
        public void foo() {
            System.out.println("target2 foo");
        }

        public void bar() {
            System.out.println("target2 bar");
        }
    }
}

package com.study.spring.s16;

import org.springframework.aop.aspectj.AspectJExpressionPointcut;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class S16 {
    public static void main(String[] args) {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        /*
        底层切点实现匹配方式：调用 aspectj 的匹配方法
        实现了 MethodMatcher 接口，用来执行方法的匹配
         */
    }
}

package com.study.spring.s14;

import org.springframework.cglib.core.Signature;

import java.lang.reflect.InvocationTargetException;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class TargetFastClass {
    /*
    避免使用反射
    1. 在创建method proxy 时，可以根据每个方法的名称和参数生成相关的签名编号
    2. 调用method proxy invoke 时，可以确定目前正在执行的编号，进入到正确的分支
     */
    static Signature s0 = new Signature("save", "()V");
    static Signature s1 = new Signature("save", "(I)V");
    static Signature s2 = new Signature("save", "(J)V");
    /**
     * 获取目标方法的编号
     * Class  Method        Signature return index
     * Target save()        0
     *        save(int i)   1
     *        save(long i)  2
     * @param signature 包括方法名字、参数返回值
     * @return 方法变化
     */
    public int getIndex(Signature signature){
        if (s0.equals(signature)) {
            return 0;
        } else if (s1.equals(signature)) {
            return 1;
        }else if (s2.equals(signature)) {
            return 2;
        }else {
            return -1;
        }
    }

    /**
     * 根据方法返回编号去正常调用目标对象的方法
     * @param index 编号
     * @param target 目标对象
     * @param args 方法参数数组
     * @return 方法返回对象
     */
    public Object invoke(int index, Object target, Object[] args){
        if (index == 0) {
            ((Target) target).save();
        } else if (index == 1) {
            ((Target) target).save(((int) args[0]));
        }else if (index == 2) {
            ((Target) target).save(((long) args[0]));
        }else{
            throw new RuntimeException("无此方法");
        }
        return null;
    }


    public static void main(String[] args) {
        TargetFastClass fastClass = new TargetFastClass();
        int index = fastClass.getIndex(new Signature("save", "(J)V"));
        System.out.println(index);

        fastClass.invoke(index, new Target(), new Object[]{1L});

    }
}

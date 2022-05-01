package com.study.spring.s14;

import org.springframework.cglib.core.Signature;

/**
 * 说明:
 *
 * @USER: Cgh
 * @DATE: 2022/5/1
 */
public class ProxyFastClass {

    static Signature s0 = new Signature("saveSuper", "()V");
    static Signature s1 = new Signature("saveSuper", "(I)V");
    static Signature s2 = new Signature("saveSuper", "(J)V");
    /**
     * 获取代理方法的编号
     * Class  Method             Signature return index
     * Proxy  saveSuper()        3
     *        saveSuper(int i)   4
     *        saveSuper(long i)  5
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
     * @param proxy 目标对象
     * @param args 方法参数数组
     * @return 方法返回对象
     */
    public Object invoke(int index, Object proxy, Object[] args){
        if (index == 0) {
            ((Proxy) proxy).saveSuper();
        } else if (index == 1) {
            ((Proxy) proxy).saveSuper(((int) args[0]));
        }else if (index == 2) {
            ((Proxy) proxy).saveSuper(((long) args[0]));
        }else{
            throw new RuntimeException("无此方法");
        }
        return null;
    }

    public static void main(String[] args) {
        ProxyFastClass fastClass = new ProxyFastClass();
        int index = fastClass.getIndex(new Signature("saveSuper", "()V"));
        System.out.println(index);
        fastClass.invoke(index, new Proxy(), new Object[0]);
    }
}

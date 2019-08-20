package com.example.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 动态代理
 * CGLIB 代理的生成原理是生成目标类的子类，而子类是增强过的，这个子类对象就是代理对象
 */
public class CGLIBDongProxy implements MethodInterceptor{
    private Tank tank;

    public CGLIBDongProxy(Tank tank) {
        this.tank = tank;
    }

    public Tank myCglibCreator(){
        Enhancer enhancer = new Enhancer();
        // 设置需要代理的对象 :  目标类(target) , 也是父类
        enhancer.setSuperclass(Tank.class);
        // 设置代理对象， 这是回调设计模式:  设置回调接口对象 :
        enhancer.setCallback(this); // this代表当前类的对象，因为当前类实现了Callback

        return (Tank) enhancer.create();

    }

    // 这个就是回调方法（类A的方法）
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        // 在前面做一些事情: 记录开始时间
        long start = System.currentTimeMillis();
        System.out.println("start time : " + start);

        method.invoke(tank, objects);

        // 在后面做一些事情: 记录结束时间,并计算move()运行时间
        long end = System.currentTimeMillis();
        System.out.println("end time : " + end);
        System.out.println("spend all time : " + (end - start)/1000 + "s.");
        return null;
    }

    public static void main(String[] args){
        Tank proxyTank = new CGLIBDongProxy(new Tank()).myCglibCreator();
        proxyTank.move();
    }
}
class Tank   {

    public void move() {
        // 坦克移动
        System.out.println("Tank Moving......");
    }
}

package com.example.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * JDK动态代理,基于接口
 */
public class JDKDongProxy implements InvocationHandler {
    private Object object;

    public JDKDongProxy(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        //doSomething
        System.out.println("初始化一辆Car");
        method.invoke(object,objects);
        System.out.println("生产结束");
        return null;
    }

    public static void main(String[] args) {
        Color color = new RedCar();
        JDKDongProxy proxy=new JDKDongProxy(color);
        Color o = (Color) Proxy.newProxyInstance(color.getClass().getClassLoader(),
                color.getClass().getInterfaces(),
                proxy);
        o.yanSe();

        System.out.println("--------------------");

        Car2 car2 = new Byd2();
        JDKDongProxy proxy1 = new JDKDongProxy(car2);
        Car2 c = (Car2) Proxy.newProxyInstance(car2.getClass().getClassLoader(),
                car2.getClass().getInterfaces(),
                proxy1);
        c.Move();
    }
}

interface Car2{
    void Move();
}

interface Color {
    void yanSe();
}

class Byd2 implements Car2{

    @Override
    public void Move() {
        System.out.println("BYD");
    }
}
class RedCar implements Color{

    @Override
    public void yanSe() {
        System.out.println("red");
    }
}

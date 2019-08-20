package com.example.proxy;

/**
 * 静态代理
 */
public class StaticProxy implements Car{
    Car car;
    StaticProxy(Car car){
        this.car=car;
    }

    @Override
    public void Move() {
        System.out.println("新出版的");//doSomething
        car.Move();
    }

    public static void main(String[] args) {
        Car car = new StaticProxy(new Byd());
        car.Move();
    }
}
interface Car{
    void Move();
}
class Byd implements Car{

    @Override
    public void Move() {
        System.out.println("BYD");
    }
}

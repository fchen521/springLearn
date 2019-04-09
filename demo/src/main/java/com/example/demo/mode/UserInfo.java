package com.example.demo.mode;

import lombok.Data;
import lombok.NonNull;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Optional;

public class UserInfo implements Cloneable{
    private int id;
    private String name;
    private String pwd;

    public UserInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


class tmain{
    public static void main(String[] args) throws CloneNotSupportedException {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("config\\test.xml");
        UserInfo info = context.getBean(UserInfo.class);
        System.out.println(info.toString());
        context.close();

       System.setProperty("spring","classpath");
        System.out.println(System.getProperties());

    }
}
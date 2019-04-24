package com.example.miaosha.miaosha.model;


import javax.persistence.Table;

@Table(name = "userinfo")
public class UserInfoPO2 {
    private Integer id;

    private String name;

    private String age;

    private String aaa;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAaa() {
        return aaa;
    }

    public void setAaa(String aaa) {
        this.aaa = aaa;
    }

    @Override
    public String toString() {
        return "UserInfoPO2{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", aaa='" + aaa + '\'' +
                '}';
    }
}
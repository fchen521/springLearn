package com.example.miaosha.miaosha.model;


import javax.persistence.Table;

@Table(name = "userinfo")
public class UserInfoPO {
    private Integer id;

    private String name;

    private Integer age;

    private String sex;

    private String context;

    private String fylx;

    private String lczd;

    private String zlh;

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
        this.name = name == null ? null : name.trim();
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? null : sex.trim();
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context == null ? null : context.trim();
    }

    public String getFylx() {
        return fylx;
    }

    public void setFylx(String fylx) {
        this.fylx = fylx == null ? null : fylx.trim();
    }

    public String getLczd() {
        return lczd;
    }

    public void setLczd(String lczd) {
        this.lczd = lczd == null ? null : lczd.trim();
    }

    public String getZlh() {
        return zlh;
    }

    public void setZlh(String zlh) {
        this.zlh = zlh == null ? null : zlh.trim();
    }

    @Override
    public String toString() {
        return "UserInfoPO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", context='" + context + '\'' +
                ", fylx='" + fylx + '\'' +
                ", lczd='" + lczd + '\'' +
                ", zlh='" + zlh + '\'' +
                '}';
    }
}
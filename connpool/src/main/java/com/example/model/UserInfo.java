package com.example.model;

import lombok.Data;

import javax.persistence.Table;

@Data
@Table(name = "userinfo")
public class UserInfo {
        private int id;
        private String name;
        private String age;
        private String sex;
        private String context;
        private String fylx;
        private int lczd;
        private int zlh;
}

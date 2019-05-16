package com.example.shiro.model;

import lombok.Data;

import javax.persistence.Table;

@Table(name = "tidtest")
@Data
public class tidtest {
    private int id;
    private int tid;
    private int tnid;

}

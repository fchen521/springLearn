package com.example.shiro.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Data
@Table(name="sys_role")
@Alias("sysRole")
public class SysRole {
    @Id
    private int id;
    private String roleName;
    private Date createTime;
    private Date updateTime;
    private String deleteStatus;



}

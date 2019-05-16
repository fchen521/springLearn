package com.example.shiro.model;

import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "sys_permission")
@Alias("sysPermission")
public class SysPermission {
    @Id
    private int id;
    private String menuCode;
    private String menuName;
    private String permissionCode;
    private String permissionName;
    private int requiredPermission;
}

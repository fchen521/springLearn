package com.example.shiro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.ibatis.type.Alias;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@Table(name="sys_user")
@Alias("sysUser")
public class SysUser implements Serializable{

    private SysRole sysRole;

    @Id
    private int id;
    private String username;
    @JsonIgnore
    private String password;
    private String nickname;
    private int roleId;
    private Date createTime;
    private Date updateTime;
    private String deleteStatus;

    @Transient
    private SysRole roles;

    @Transient
    private List<SysPermission> permissions;

    @Transient
    private String code;

    @Data
    public static class ResponseU {
        private String username;
        private String password;
        private String code;
    }
}

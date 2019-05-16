package com.example.shiro.controller;

import com.example.shiro.model.SysUser;
import com.example.shiro.response.ErrorEnum;
import com.example.shiro.response.ResponseData;
import com.example.shiro.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.omg.PortableInterceptor.SUCCESSFUL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class testController {
   /* @Autowired
    private UserService userService;

    @GetMapping(value = "/list")
    @RequiresPermissions("article:list")
    public ResponseData UserInfo(@RequestParam(required = false) String name){
        SysUser user=new SysUser();
        user.setRoleId(1);
       return new ResponseData<SysUser>(200,"succ",userService.getUserByName(name));
    }*/
}

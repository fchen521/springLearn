package com.example.shiro.service;

import com.example.shiro.mapper.UserMapper;
import com.example.shiro.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void getUserByName(String userName){
      //return userMapper.getUserDetails(userName);
    }
}

package com.example.controller;

import com.example.mapper.UserInfoMapper;
import com.example.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hello")
public class UserController {
    @Autowired
    UserInfoMapper mapper;

    @GetMapping("/")
    public String hello(){
       return "succ";
    }
}

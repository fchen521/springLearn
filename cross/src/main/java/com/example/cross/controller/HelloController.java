package com.example.cross.controller;

import com.sun.xml.internal.ws.client.sei.ResponseBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 跨域访问第一种解决方式，通过注解
 * @CrossOrigin
 */
@RestController
@RequestMapping("/test")
public class HelloController {

    @GetMapping
    public String test(){
        System.out.println("into");
        return "test";
    }
}

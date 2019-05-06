package com.example.thymeleaf.service;

import com.example.thymeleaf.mapper.TUsersPoMapper;
import com.example.thymeleaf.model.TUsersPo;
import com.example.thymeleaf.response.ResponseEntity;
import com.example.thymeleaf.utils.TaleUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    TUsersPoMapper mapper;

    public TUsersPo login(String username,String pwd) throws Exception {
        if (StringUtils.isBlank(username) || StringUtils.isBlank(pwd)) {
            throw new Exception("用户名和密码不能为空");
        }
        String password = TaleUtils.MD5encode(username+pwd);
        TUsersPo usersPo = new TUsersPo();
        usersPo.setUsername(username);usersPo.setPassword(password);
        return mapper.selectOne(usersPo);
    }
}

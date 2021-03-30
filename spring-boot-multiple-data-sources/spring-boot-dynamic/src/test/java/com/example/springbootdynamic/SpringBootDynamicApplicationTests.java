package com.example.springbootdynamic;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootdynamic.mapper.D2UserMapper;
import com.example.springbootdynamic.mapper.UserMapper;
import com.example.springbootdynamic.mode.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = SpringBootDynamicApplication.class)
public class SpringBootDynamicApplicationTests {
    @Autowired
    UserMapper userMapper;

    @Autowired
    D2UserMapper d2UserMapper;

    @Test
    public void testMapper() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        List<User> users1 = userMapper.selectList(wrapper);
        users1.stream().forEach(x -> System.out.println("name:"+x.getName()+" "+x.getAge()));

        LambdaQueryWrapper<User> wrapper2 = Wrappers.lambdaQuery();
        List<User> users2 = d2UserMapper.selectList(wrapper2);
        users2.stream().forEach(x -> System.out.println("name:"+x.getName()+" "+x.getAge()));
    }

}

package com.example.springbootmybatisplus;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.springbootmybatisplus.mapper.db1.D1UserMapper;
import com.example.springbootmybatisplus.mapper.db2.UserMapper;
import com.example.springbootmybatisplus.mode.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest(classes = SpringBootMybatisPlusApplication.class)
public class SpringBootMybatisPlusApplicationTests {



    @Autowired
    D1UserMapper d1UserMapper;

    @Autowired
    UserMapper mapper;
    @Test
    public void testUsers() {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery();
        List<User> users1 = d1UserMapper.selectList(wrapper);
        users1.stream().forEach(x -> System.out.println("name:"+x.getName()+" "+x.getAge()));

        List<User> users = mapper.selectList(Wrappers.lambdaQuery());
        users.stream().forEach(x -> System.out.println("name:"+x.getName()+" "+x.getAge()));
    }

}

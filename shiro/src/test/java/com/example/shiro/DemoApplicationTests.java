package com.example.shiro;

import com.example.shiro.mapper.UserMapper;
import com.example.shiro.model.SysUser;
import com.example.shiro.model.tidtest;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {
	@Autowired
	UserMapper userMapper;
	@Test
	public void contextLoads() {

		List<tidtest> all = userMapper.selectAll();
		all.forEach(a->{
			System.out.println(a.getId());
		});
	}

}

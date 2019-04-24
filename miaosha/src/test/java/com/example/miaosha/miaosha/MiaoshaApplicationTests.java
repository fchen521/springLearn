package com.example.miaosha.miaosha;

import com.example.miaosha.miaosha.mapper.UserInfoPOMapper;
import com.example.miaosha.miaosha.model.UserInfoPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MiaoshaApplicationTests {

	@Autowired
	UserInfoPOMapper mapper;

	@Test
	public void contextLoads() {
		List<UserInfoPO> pos = mapper.selectAll();
		System.out.println(pos.size());

	}

}

package com.example.springbootmybatisplus.mapper.db1;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatisplus.mode.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface D1UserMapper extends BaseMapper<User> {
}

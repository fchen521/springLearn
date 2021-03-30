package com.example.springbootmybatisplus.mapper.db2;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootmybatisplus.mode.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}

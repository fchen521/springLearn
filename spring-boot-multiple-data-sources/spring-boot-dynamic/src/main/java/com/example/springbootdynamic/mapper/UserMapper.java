package com.example.springbootdynamic.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springbootdynamic.mode.User;
import org.springframework.stereotype.Repository;

/**
 * @author chen fei
 * @version 1.0
 * @desc
 * @date 2021/3/30 16:30
 */
@org.apache.ibatis.annotations.Mapper
@Repository
@DS("master")
public interface UserMapper extends BaseMapper<User> {
}

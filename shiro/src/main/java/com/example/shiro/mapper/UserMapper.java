package com.example.shiro.mapper;


import com.example.shiro.model.tidtest;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper extends tk.mybatis.mapper.common.Mapper<tidtest>{

}

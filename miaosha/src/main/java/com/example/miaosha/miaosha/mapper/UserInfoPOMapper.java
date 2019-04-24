package com.example.miaosha.miaosha.mapper;


import com.example.miaosha.miaosha.model.UserInfoPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface UserInfoPOMapper extends Mapper<UserInfoPO>{

}
package com.example.mapper;

import com.example.model.UserInfo;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface UserInfoMapper extends Mapper<UserInfo>{
}

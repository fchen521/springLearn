package com.example.thymeleaf.mapper;

import com.example.thymeleaf.model.TOptionsPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface TOptionsPOMapper extends Mapper<TOptionsPO>{

}
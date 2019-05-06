package com.example.thymeleaf.mapper;

import com.example.thymeleaf.model.TLogsPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface TLogsPOMapper extends Mapper<TLogsPO>{

}
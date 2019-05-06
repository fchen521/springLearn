package com.example.thymeleaf.mapper;

import com.example.thymeleaf.model.TMetasPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface TMetasPOMapper extends Mapper<TMetasPO>{

}
package com.example.thymeleaf.mapper;

import com.example.thymeleaf.model.TAttachPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface TAttachPOMapper extends Mapper<TAttachPO>{
}
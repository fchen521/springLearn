package com.example.thymeleaf.mapper;

import com.example.thymeleaf.model.TCommentsPO;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
@org.apache.ibatis.annotations.Mapper
public interface TCommentsPOMapper extends Mapper<TCommentsPO> {

}
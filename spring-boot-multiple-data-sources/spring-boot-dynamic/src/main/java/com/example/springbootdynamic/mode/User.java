package com.example.springbootdynamic.mode;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author chen fei
 * @version 1.0
 * @desc
 * @date 2021/3/30 16:27
 */
@Data
@TableName("multi_user")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = -1923859222295750467L;

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 年龄
     */
    private Integer age;
}
package com.example.zdy_interface;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Target 说明了Annotation所修饰的对象范围 TYPE(类、接口、枚举、Annotation类型) FIELD字段 METHOD方法 等
 * Retention 定义被保留的时长 SOURCE:在源文件中有效（即源文件保留），CLASS:在class文件中有效（即class保留），RUNTIME:在运行时有效（即运行时保留
 * Documented 该注解包含在javadoc工具中
 *
 */
@Target({ElementType.TYPE,ElementType.FIELD,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ZdyInterface {
    int id() default -1;

    String name() default "小明";
}

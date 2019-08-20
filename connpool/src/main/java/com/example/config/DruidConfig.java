package com.example.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @EnableConfigurationProperties 使用的时候要在启动类中启用配置
 * @PropertySource("classpath:xxx.properties") 用来指定配置文件
 * @ConfigurationProperties("xxx") 用来指定配置匹配规则
 */
@Configuration
public class DruidConfig {
    @Primary
    @Bean
    @ConfigurationProperties("spring.datasource.druid")
    public DataSource dataSource(){
        return DruidDataSourceBuilder.create().build();
    }

}

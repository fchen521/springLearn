package com.example.springbootmybatisplus.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

/**
 * @author chen fei
 */
@Configuration
public class DataSourceConfig {
    /**
     * 主数据源配置
     * @return
     */
    @Primary
    @Bean("db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource db1DataSource(){
        return DataSourceBuilder.create().build();
    }

    /**
     * 第二个ds2数据源
     * @return
     */
    @Bean("db2DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db2")
    public DataSource ds2DataSource() {
        return DataSourceBuilder.create().build();
    }
}

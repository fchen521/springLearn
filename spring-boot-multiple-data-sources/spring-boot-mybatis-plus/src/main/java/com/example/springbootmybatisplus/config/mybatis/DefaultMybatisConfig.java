package com.example.springbootmybatisplus.config.mybatis;

import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.MybatisXMLLanguageDriver;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author chen fei
 * @version 1.0
 * @desc
 * @date 2021/3/19 17:06
 */
@Configuration
@MapperScan(basePackages = "com.example.springbootmybatisplus.mapper.db1",sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DefaultMybatisConfig {
    //主数据源 ds1数据源
    @Primary
    @Bean("db1SqlSessionFactory")
    public SqlSessionFactory db1SqlSessionFactory(@Qualifier("db1DataSource") DataSource dataSource) throws Exception {
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        MybatisConfiguration configuration = new MybatisConfiguration();
        //setTypeAliasesPackage表示扫描该包下java文件作为别名 , 即mapper.xml文件中 select 标签 resultType属性的值 ,不设置则要输入对应实体的全限定名称 , 设置只需输入类名即可
        bean.setTypeAliasesPackage("com.example.springbootmybatisplus.mode");
        configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
        configuration.setJdbcTypeForNull(JdbcType.NULL);
        bean.setConfiguration(configuration);
//        bean.setMapperLocations(new PathMatchingResourcePatternResolver().
//                getResources("classpath*:mapper/db2/**/*.xml"));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "db1TransactionManager")
    public DataSourceTransactionManager db1TransactionManager(@Qualifier("db1DataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Primary
    @Bean(name = "db1SqlSessionTemplate")
    public SqlSessionTemplate db1SqlSessionTemplate(@Qualifier("db1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

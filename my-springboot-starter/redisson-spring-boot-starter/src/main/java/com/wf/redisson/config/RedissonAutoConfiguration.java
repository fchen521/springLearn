package com.wf.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chen fei
 * @version 1.0
 * @desc
 * @date 2021/2/23 10:24
 */
// @Configuration用于定义配置类，可替换xml配置文件，被注解的类内部包含有一个或多个被@Bean注解的方法，
// 这些方法将会被AnnotationConfigApplicationContext或AnnotationConfigWebApplicationContext类进行扫描，
// 并用于构建bean定义，初始化Spring容器。
@Configuration

@ConditionalOnClass(Redisson.class)

//RedissonProperties类只使用了@ConfigurationProperties注解，然后该类没有在扫描路径下或者没有使用@Component等注解，导致无法被扫描为bean，
// 那么就必须在配置类上使用@EnableConfigurationProperties注解去指定这个类，这个时候就会让该类上的@ConfigurationProperties生效，然后作为
// bean添加进spring容器中
@EnableConfigurationProperties(RedissonProperties.class)
public class RedissonAutoConfiguration {
    @Bean
    public RedissonClient redissonClient(RedissonProperties properties){
        Config config = new Config();
        String prefix = "redis://";
        // 是否加密
        if (properties.isSsl()) {
            prefix = "rediss://";
        }
        String address = prefix + properties.getHost() + ":" + properties.getProt();
        config.useSingleServer().setAddress(address).setConnectTimeout(properties.getTimeout());
        return Redisson.create(config);
    }
}

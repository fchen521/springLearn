package com.redisson.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author chen fei
 * @version 1.0
 * @desc
 * @date 2021/2/23 14:04
 */
@RestController
public class RedissonController {

    @Autowired
    RedissonClient redissonClient;

    @GetMapping("set")
    public String set(String msg){
        RBucket<Object> bucket = redissonClient.getBucket("name");
        if(bucket.get() == null){
            bucket.set(msg);
        }
        return bucket.get().toString();
    }
}

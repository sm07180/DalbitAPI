package com.dalbit.redis.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class RedisDao {

    @Autowired
    private RedisTemplate redisTemplate;

    public String getMemberInfo(){
        redisTemplate.opsForList().getOperations();
        return "";
    }

    public ValueOperations<String, Object> getOpsForValue(){
        return redisTemplate.opsForValue();
    }
}

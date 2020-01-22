package com.dalbit.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisUtil {

    @Autowired
    RedisTemplate redisTemplate;

    @Value("${spring.cache.redis.key.prefix}")
    String SPRING_CACHE_REDIS_KEY_PREFIX;

    public boolean isExistLoginSession(String memNo){
        return 0 < redisTemplate.opsForHash().getOperations().boundSetOps(SPRING_CACHE_REDIS_KEY_PREFIX + memNo).size();
    }
}

package com.dalbit.config;

import com.dalbit.util.DalbitUtil;
import io.lettuce.core.ReadFrom;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.session.ReactiveMapSessionRepository;
import org.springframework.session.ReactiveSessionRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Redis 설정
 */
@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisRepositoryConfig {

    @Value("${spring.redis.master.host}")
    private String REDIS_MASTER_HOST;

    @Value("${spring.redis.master.port}")
    private int REDIS_MASTER_PORT;

    @Value("${spring.redis.password}")
    private String REDIS_PASSWORD;

    @Value("${spring.redis.database}")
    private int REDIS_DATABASE;

    @Value("${spring.redis.slave.info}")
    private String REDIS_SLAVE_INFO;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {

        List<HashMap> slaveList = new ArrayList();

        try {
            for (String slaveInfo : REDIS_SLAVE_INFO.split(",")) {
                HashMap map = new HashMap();
                map.put("slaveHost", slaveInfo.split(":")[0]);
                map.put("slavePort", slaveInfo.split(":")[1]);

                slaveList.add(map);
            }
        }catch (Exception e){
            log.error("Redis slave setting Exception : confirm .properties [spring.redis.slave.info]");
        }

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
            .readFrom(DalbitUtil.isEmpty(slaveList) ? ReadFrom.MASTER_PREFERRED : ReadFrom.REPLICA_PREFERRED)
            .build();

        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(REDIS_MASTER_HOST, REDIS_MASTER_PORT);
        staticMasterReplicaConfiguration.setPassword(REDIS_PASSWORD);
        staticMasterReplicaConfiguration.setDatabase(REDIS_DATABASE);

        slaveList.forEach(slaveInfo -> {
            staticMasterReplicaConfiguration.addNode(DalbitUtil.getStringMap(slaveInfo, "slaveHost"), DalbitUtil.getIntMap(slaveInfo, "slavePort"));
        });

        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        //redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(RedisData.class));
        return redisTemplate;
    }

    @Bean
    public TaskScheduler taskScheduler() {
        return new ConcurrentTaskScheduler();
    }

    @Bean
    public ReactiveSessionRepository reactiveSessionRepository() {
        return new ReactiveMapSessionRepository(new ConcurrentHashMap<>());
    }

}

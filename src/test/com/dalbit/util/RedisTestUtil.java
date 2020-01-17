package com.dalbit.util;

import com.dalbit.common.vo.RedisData;
import com.dalbit.redis.dao.RedisDao;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class RedisTestUtil {

    @Autowired
    RedisDao redisDao;

    @Test
    public void 레디스테스트(){

        //get/set을 위한 객체
        ValueOperations<String, Object> vop = redisDao.getOpsForValue();
        //자료형 생성
        /*
        RedisData setData = new RedisData();
        setData.setItemId("redisTest");
        setData.setSourceId("레디스테스트중입니다.");
        */
        //set
        //vop.set("key", setData);
        RedisData getData = (RedisData) vop.get("key");
        System.out.println(getData.getItemId());//jeong
        System.out.println(getData.getSourceId());//pro

    }


}

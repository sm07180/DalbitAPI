package com.dalbit.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class DalbitUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    public void 더블인트파싱(){
        HashMap map = new HashMap();
        map.put("amt", "19.0");

        int result = DalbitUtil.getIntMap(map, "amt");

        Assert.assertEquals(result, 19);
    }

    @Test
    public void 회원번호생성(){
        jwtUtil.generateToken("21590930164578", true);
    }


}

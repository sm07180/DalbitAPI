package com.dalbit.util;

import com.dalbit.member.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class PropertyTestUtil {

    @Test
    public void 프로퍼티_읽기(){
        log.debug(DalbitUtil.getProperty("sso.domain"));
    }
}

package com.demo.member;

import com.demo.member.service.MemberService;
import com.demo.member.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void 로그인테스트(){
        log.debug("로그이넽스트");
    }

    @Test
    public void build테스트(){
        log.debug("ㅁ니ㅏ어림넝리ㅏㅓ밍러미어");
        log.debug(LoginVo.builder().build().toString());
    }

}

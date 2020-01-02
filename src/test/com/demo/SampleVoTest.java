package com.demo;

import com.demo.sample.service.SampleService;
import com.demo.util.AES;
import com.demo.util.CipherUtil;
import com.demo.util.JwtUtil;
import com.demo.util.RestApiUtil;
import com.demo.common.vo.SampleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.security.Security;

@Slf4j
@SpringBootTest
@ActiveProfiles({"local"})
public class SampleVoTest {

    @Value("${active.profile.name}")
    private String profileName;

    @Autowired
    private SampleService sampleService;

    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void test(){
        log.debug(profileName);
    }

    @Test
    public void 암복호화테스트() throws Exception {

        final String secretKey = "ssshhhhhhhhhhh!!!!";

        Security.setProperty("crypto.policy", "unlimited");

        String text = "안녕하세요";

        String encText = AES.encrypt(text, secretKey);
        log.debug(encText);

        String decText = AES.decrypt(encText, secretKey);
        log.debug(decText);
    }

    @Test
    public void 단방향암호화() throws Exception {
        String text = "1234";
        String salt = "leejaeho";
        String salt2 = "leejaeho1";

        log.debug(CipherUtil.encryptSha(CipherUtil.CIPHER_ALGORITHM.SHA256, text));
        log.debug(CipherUtil.encryptSha(CipherUtil.CIPHER_ALGORITHM.SHA256, text));

        log.debug(CipherUtil.encryptSha(CipherUtil.CIPHER_ALGORITHM.SHA512, text));
        log.debug(CipherUtil.encryptSha(CipherUtil.CIPHER_ALGORITHM.SHA512, text));

        log.debug(CipherUtil.encryptSha256WithSalt(text, salt));
        log.debug(CipherUtil.encryptSha256WithSalt(text, salt));
        log.debug(CipherUtil.encryptSha256WithSalt(text, salt2));
        log.debug(CipherUtil.encryptSha256WithSalt(text, salt2));


        log.debug(CipherUtil.encryptSha256WithSalt("leejaeho114", "1234"));
        log.debug(CipherUtil.encryptSha256WithSalt("leejaeho114", "1234"));
    }

    @Test
    public void 트랜젝션(){
        SampleVo sampleVo = new SampleVo();
        sampleVo.setId("transaction");
        sampleVo.setName("테스트");
        sampleVo.setAge(20);

        sampleService.transactionTest(sampleVo);
    }

    @Test
    public void restApi테스트(){
        RestApiUtil.sendGet("https://devm-leejaeho1144.wawatoc.com/errorTestData");
    }

    @Test
    public void restApiPOST테스트(){
        RestApiUtil.sendPost("https://devm-leejaeho1144.wawatoc.com/sample", "");
    }

    @Test
    public void jwt토큰생성(){
        String token = jwtUtil.generateToken("test");
        log.info("JWT 토큰 : " + token);
        String userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        boolean isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);


        token = jwtUtil.generateToken("leejaeho114");
        log.info("JWT 토큰 : " + token);
        userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);



        token = jwtUtil.generateToken("yosi");
        log.info("JWT 토큰 : " + token);
        userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);
    }

    @Test
    public void jwt토큰검증(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0ZXN0IiwiaWF0IjoxNTc3OTI1NDA4LCJleHAiOjE1Nzc5MjU0MTh9.m2BLN9HTWgAq0nFR7iO2wUnd8uYpO2Bic5ccMSq-hkM";
        boolean isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);


        token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJsZWVqYWVobzExNCIsImlhdCI6MTU3NzkyNTQwOSwiZXhwIjoxNTc3OTI1NDE5fQ.uJqwwmAVYM-yQGrAl3rXv3NpBO6s66MzeB1DCo-CfC4";
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);

        token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ5b3NpIiwiaWF0IjoxNTc3OTI1NDA5LCJleHAiOjE1Nzc5MjU0MTl9.re1RUadDovgL6dCOJPHwReenhwV4uRK6pcfXwG5oUj0";
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);
    }

}

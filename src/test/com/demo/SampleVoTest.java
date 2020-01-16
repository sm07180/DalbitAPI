package com.demo;

import com.demo.sample.service.SampleService;
import com.demo.util.*;
import com.demo.sample.vo.SampleVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.Security;

import static org.assertj.core.api.Assertions.assertThat;

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
        RestApiUtil.sendPost("https://devm-leejaeho1144.wawatoc.com:4431/xssTest", "memid=<script>12345dd</script>");
    }

    @Test
    public void jwt토큰생성(){
        String token = jwtUtil.generateToken("010-1234-4568");
        log.info("JWT 토큰 : " + token);
        String userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        boolean isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);


        token = jwtUtil.generateToken("010-1234-4568");
        log.info("JWT 토큰 : " + token);
        userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);



        token = jwtUtil.generateToken("010-1234-4568");
        log.info("JWT 토큰 : " + token);
        userId = jwtUtil.getUserNameFromJwt(token);
        log.info("JWT FROM ID : " + userId);
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);
    }

    @Test
    public void jwt토큰검증(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIwMTAtMTIzNC00NTY4IiwiaWF0IjoxNTc4ODkyODQ3LCJleHAiOjE1Nzg4OTU0Mzl9.D7KMNYeBi-3VyhVyhcFc7H2Ap9MVKD3OTgpaPe_bBjw";
        boolean isValid = jwtUtil.validateToken(token);
        log.debug("isValid : {}", isValid);
        log.debug("가져온 이름 : " + jwtUtil.getUserNameFromJwt(token));


       /* token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJsZWVqYWVobzExNCIsImlhdCI6MTU3NzkyNTQwOSwiZXhwIjoxNTc3OTI1NDE5fQ.uJqwwmAVYM-yQGrAl3rXv3NpBO6s66MzeB1DCo-CfC4";
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);

        token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ5b3NpIiwiaWF0IjoxNTc3OTI1NDA5LCJleHAiOjE1Nzc5MjU0MTl9.re1RUadDovgL6dCOJPHwReenhwV4uRK6pcfXwG5oUj0";
        isValid = jwtUtil.validateToken(token);
        log.debug("isValid : " + isValid);*/
    }



        private TestRestTemplate restTemplate;

   /*     @Test
        public void 태그_치환() {
            String content = "<li>content</li>";
            String expected = "&lt;li&gt;content&lt;/li&gt;";
            ResponseEntity<XssRequestDto> response = restTemplate.postForEntity(
                    "/xss",
                    new XssRequestDto(content),
                    XssRequestDto.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).isEqualTo(expected);
        }*/

        @Test
        public void application_form_전송() {
            String content = "<li>content</li>";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            map.add("content", content);

            HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);

            ResponseEntity<String> response = restTemplate.exchange("/form",
                    HttpMethod.POST,
                    entity,
                    String.class);

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody()).isEqualTo(content);
        }

        /*@Test
        public void LocalDate가_치환된다() throws Exception {
            String content = "<li>content</li>";
            String expected = "&lt;li&gt;content&lt;/li&gt;";
            LocalDate requestDate = LocalDate.of(2019,12,29);
            ResponseEntity<XssRequestDto2> response = restTemplate.postForEntity(
                    "/xss2",
                    new XssRequestDto2(content, requestDate),
                    XssRequestDto2.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(response.getBody().getContent()).isEqualTo(expected);
            assertThat(response.getBody().getRequestDate()).isEqualTo(requestDate);
        }*/

    @Test
    public void 랜덤문자열생성_테스트(){

        log.warn("string");
        log.info(CommonUtil.randomValue("string", 4));
        log.info(CommonUtil.randomValue("string", 4));

        log.warn("p");
        log.info(CommonUtil.randomValue("p", 4));
        log.info(CommonUtil.randomValue("p", 4));

        log.warn("c");
        log.info(CommonUtil.randomValue("c", 4));
        log.info(CommonUtil.randomValue("c", 4));

        log.warn("a");
        log.info(CommonUtil.randomValue("a", 4));
        log.info(CommonUtil.randomValue("a", 4));

        log.warn("number");
        log.info(CommonUtil.randomValue("number", 4));
        log.info(CommonUtil.randomValue("number", 4));
    }

}



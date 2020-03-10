package com.dalbit;

import com.dalbit.common.vo.LocationVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.sample.service.SampleService;
import com.dalbit.sample.vo.SampleVo;
import com.dalbit.util.*;
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

import javax.servlet.http.HttpServletRequest;
import java.security.Security;
import java.util.Calendar;
import java.util.Date;

import static jdk.nashorn.internal.objects.NativeString.substr;
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
    public void jwt토큰생성() throws GlobalException {
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
    public void jwt토큰검증() throws GlobalException {
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
        log.info(DalbitUtil.randomValue("string", 4));
        log.info(DalbitUtil.randomValue("string", 4));

        log.warn("p");
        log.info(DalbitUtil.randomValue("p", 4));
        log.info(DalbitUtil.randomValue("p", 4));

        log.warn("c");
        log.info(DalbitUtil.randomValue("c", 4));
        log.info(DalbitUtil.randomValue("c", 4));

        log.warn("a");
        log.info(DalbitUtil.randomValue("a", 4));
        log.info(DalbitUtil.randomValue("a", 4));

        log.warn("number");
        log.info(DalbitUtil.randomValue("number", 4));
        log.info(DalbitUtil.randomValue("number", 4));
    }

    @Test
    public void 테스트(HttpServletRequest request){

        LocationVo locationVo = DalbitUtil.getLocation(request);

        log.debug(locationVo.toString());
    }

    @Test
    public void replaceTest(){
        String tempUrl = "/temp/2020/02/03/10/20200203102802930921.jpg";

        String replacePath = tempUrl.replace("/temp", "");

        log.debug(replacePath);
    }

    @Test
    public void value(){
        String temp = "123";
        int number =  3;
        validaionCheck(temp, number);
    }

    @Test
    public void validaionCheck(String temp, int number){
        int length = temp.length();
        log.debug(length+"");
        log.debug((int)Math.log10(number)+1+"");
    }

    @Test void 비밀번호패턴체크(){
        String pw = "12515asd";
        Boolean isPassword = DalbitUtil.isPasswordCheck(pw);
        log.info("패턴체크: {}", isPassword);
    }

    @Test void serverip(){
        Date startDate = new Date(System.currentTimeMillis());
        Date endDate = new Date(System.currentTimeMillis() + 2592000000L);

        endDate.before(startDate);


    }

    @Test
    public void 테스트(){
        String birth = getBirth("1989", "9", "7");
        log.info(birth);
    }

    @Test
    public String getBirth(String year, String month, String day){
        month = (month.length() == 1) ? "0"+month : month;
        day = (day.length() == 1) ? "0"+day : day;

        String birth = year+month+day;

        return birth;

    }

    @Test
    public void 각종테스트(){
        String str = "yyyyMMdd";

        /*String year = substr(str,0,4);
        String month = substr(str, 4,2);
        String day = substr(str,6,2);

        log.info(DalbitUtil.convertDate(str, "yyyy-MM-dd"));*/

        log.debug("오늘날짜: {}", DalbitUtil.getDate(str));
        log.debug("3 일후: {}", DalbitUtil.getDate(str, 3));
        log.debug("7 일전: {}", DalbitUtil.getDate(str, -7));

    }

}


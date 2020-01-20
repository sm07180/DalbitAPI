package com.dalbit.sample.controller;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.sample.service.SampleService;
import com.dalbit.sample.vo.SampleVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class SampleRestController {

    @Autowired
    SampleService sampleService;

    @Autowired
    MemberService memberService;

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 권한 처리 테스트
     * USER, ADMIN 접근가능
     * @return
     */
    @Secured({"ROLE_USER", "ROLE_ADMIN"})
    @GetMapping("rest")
    public String rest(){

        List<SampleVo> list = sampleService.getList();
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    /**
     * USER만 접근 가능
     * @return
     */
    @GetMapping("allowRest")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String allowRest(){
        List<SampleVo> list = sampleService.getList();
        list.addAll(list);

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }


    /**
     * USER만 접근 가능
     * @return
     */
    @GetMapping("rejectNotUserRest")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String rejectNotUserRest(){
        List<SampleVo> list = sampleService.getList();

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    /**
     * ADMIN만 접근 가능
     * @return
     */
    @GetMapping("rejectNotAdminRest")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String rejectNotAdminRest(){
        List<SampleVo> list = sampleService.getList();
        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, list));

        log.debug(result);

        return result;
    }

    @GetMapping("errorTest")
    public String errorTest(){
        return gsonUtil.toJson(new JsonOutputVo(ErrorStatus.권한없음));
    }


    @GetMapping("errorTestData")
    public String errorTestData(){

        HashMap map = new HashMap();
        map.put("aa", "에이");
        map.put("bb", "비비");
        map.put("cc", "씨씨");

        return gsonUtil.toJson(new JsonOutputVo(ErrorStatus.잘못된호출, map));
    }

    @GetMapping("exception1")
    public String exception1(){

        SampleVo sampleVo = new SampleVo();
        sampleVo.setId("transaction");
        sampleVo.setName("테스트");
        sampleVo.setAge(20);

        sampleService.transactionTest(sampleVo);

        return gsonUtil.toJson(new JsonOutputVo(ErrorStatus.권한없음, null));
    }

    /**
     * Swagger Sample
     * @param gubun
     * @param gubunNM
     * @param title
     * @return
     */
    @PostMapping("brodTest")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String brodTest(@RequestParam(value = "gubun", required = true) String gubun,
                           @RequestParam(value = "gubunNM", required = true) String gubunNM,
                           @RequestParam(value = "title", required = true) String title,
                           @RequestParam(value = "intro", required = false) String intro){

        log.debug("gubun: {} ", gubun);
        log.debug("gubunNM: {} ", gubunNM);
        log.debug("title: {} ", title);
        log.debug("intro: {} ", intro);


        HashMap map = new HashMap();
        map.put("result", "success");
        map.put("gubun", gubun);
        map.put("gubunNM", gubunNM);
        map.put("bgImg", "방송배경이미지");
        map.put("title", title);
        map.put("intro", intro);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }


    /**
     *
     * @return
     */
    @GetMapping(value = "/v1.0")
    public String version1(){

        HashMap map = new HashMap();
        map.put("version", "1");

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    @GetMapping(value = {"/v2.0", "/v2.1"})
    public String version2(){

        HashMap map = new HashMap();
        map.put("version", "2");

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, map));
    }

    @GetMapping(value = "/null")
    public String errorNull(){

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, new MemberVo()));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

    @GetMapping("log")
    public String selectLogData(HttpServletRequest request, HttpServletResponse response){
        //List<SampleVo> list = sampleService.selectLogData();

        HashMap data = new HashMap();
        data.put("스크립트", "<script>alert();</script>");
        data.put("DIV", "<div>12345</div>");
        data.put("TAG", request.getParameter("tag"));

        String result = gsonUtil.toJson(new JsonOutputVo(Status.조회, data));
        log.debug("selectLogData {}", result);

        return result;
    }

    @GetMapping(value = "/db")
    public String procedureCallTest(){

        sampleService.getCount();

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, null));
    }

    @GetMapping(value = "/jwt")
    public String jwtTokenSample(HttpServletRequest request){

        request.getSession().setAttribute(request.getParameter("key"), request.getParameter("value"));

        //redisTemplate.

        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
    }

}

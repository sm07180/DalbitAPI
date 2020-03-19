package com.dalbit.sample.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class SampleController {

    @GetMapping("login")
    public String login(){
        log.debug("로그인 입니다.");
        return "login/login";
    }

    @GetMapping("signup")
    public String signup(){
        log.debug("회원가입 입니다.");
        return "member/signup";
    }

    @GetMapping("sample/token")
    public String token(){
        log.debug("토큰체크 입니다.");
        return "sample/token";
    }

    @GetMapping("sample/auth/step1")
    public String authStep1(){
        log.debug("본인인증 Step 1");
        return "sample/kmcis_web_sample_step01";
    }

    @PostMapping("sample/auth/step2")
    public String authStep2(){
        log.debug("본인인증 Step 2");
        return "sample/kmcis_web_sample_step02";
    }


    @PostMapping("sample/auth/result")
    public String authStep3(){
        log.debug("본인인증 Step 3");
        return "sample/kmcis_web_sample_step03";
    }

    @GetMapping("sample/auth/step4")
    public String authStep4(){
        log.debug("본인인증 Step 4");
        return "sample/kmcis_web_sample_step04";
    }


}

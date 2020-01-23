package com.dalbit.sample.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class SampleController {

    @GetMapping("login")
    public String login(){
        log.debug("로그인 입니다.");
        return "/login/login";
    }

    @GetMapping("signup")
    public String signup(){
        log.debug("회원가입 입니다.");
        return "/member/signup";
    }
}

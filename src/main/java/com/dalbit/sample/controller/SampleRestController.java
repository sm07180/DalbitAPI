package com.dalbit.sample.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.sample.vo.SampleVo;
import com.dalbit.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.Oneway;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("rest/sample")
public class SampleRestController {

    @Autowired
    MessageUtil messageUtil;

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("tokenCheck")
    public String tokenCheck(HttpServletRequest request) throws GlobalException {

        TokenVo tokenVo = null;

        String authToken = request.getParameter("authToken");
        if(jwtUtil.validateToken(authToken)){
            tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
        }


        return gsonUtil.toJson(new JsonOutputVo(Status.조회, tokenVo));
    }

    @PostMapping("getRedisData")
    public String getRedisData(HttpServletRequest request) throws GlobalException {

        TokenVo tokenVo = null;

        String authToken = request.getParameter("authToken");
        if(jwtUtil.validateToken(authToken)){
            tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
        }

        MemberVo memberVo = redisUtil.getMemberInfoFromRedis(tokenVo.getMemNo());
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, memberVo));
    }

    @GetMapping("validation")
    public String validation(
            @RequestParam("param") @NotNull int param
    )throws GlobalException {

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, param));
    }

    @GetMapping("validation2")
    public String validation2(
            @RequestParam("param") @Min(4) String param
    )throws GlobalException {
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, param));
    }

    @GetMapping("checkMe")
    public String checkMe(HttpServletRequest request)throws GlobalException{

        MemberVo memberVo = redisUtil.getMemberInfo();

        String authToken = request.getHeader(DalbitUtil.getProperty("sso.header.cookie.name"));
        if(jwtUtil.validateToken(authToken)){
            TokenVo tokenVo = jwtUtil.getTokenVoFromJwt(authToken);
            if(tokenVo.getMemNo().equals(memberVo.getMemNo())){
                return gsonUtil.toJson(new JsonOutputVo(Status.조회, "본인입니다."));
            }
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, "본인이 아닙니다."));
    }

    @GetMapping("nameCheck")
    public String nameCheck(@Valid SampleVo sampleVo, BindingResult bindingResult){

        return gsonUtil.toJson(new JsonOutputVo(Status.조회));
    }
}

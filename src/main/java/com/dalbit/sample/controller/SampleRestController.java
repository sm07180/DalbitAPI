package com.dalbit.sample.controller;

import com.dalbit.common.code.ErrorStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.sample.service.SampleService;
import com.dalbit.sample.vo.SampleVo;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.JwtUtil;
import com.dalbit.util.MessageUtil;
import com.dalbit.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Member;
import java.util.HashMap;
import java.util.List;

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
}

package com.demo.member.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.GlobalException;
import com.demo.member.service.MemberService;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_InfoVo;
import com.demo.member.vo.P_LoginVo;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
public class MemberController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    private GsonUtil gsonUtil;
    @Autowired
    MemberService memberService;

    /**
     * 회원정보 조회
     */
    @ApiOperation(value = "회원정보 조회")
    @GetMapping("/mypage")
    public String infoView() throws GlobalException {

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .target_mem_no(MemberVo.getUserInfo().getMemNo())
                .build();

        String result = memberService.callMemberInfoView(apiData);

        return result;
    }

    /**
     * 회원정보 조회
     */
    @ApiOperation(value = "회원정보 조회")
    @GetMapping("/profile")
    public String infoView(HttpServletRequest request) throws GlobalException{

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .target_mem_no(StringUtil.convertRequestParamToString(request, "s_mem_no"))
                .build();

        String result = memberService.callMemberInfoView(apiData);

        return result;
    }
}

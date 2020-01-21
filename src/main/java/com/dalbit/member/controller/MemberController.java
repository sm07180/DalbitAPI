package com.dalbit.member.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    MemberService memberService;

    @PostMapping("signup")
    public String signup(HttpServletRequest request){

        P_JoinVo joinVo = new P_JoinVo(
            DalbitUtil.convertRequestParamToString(request,"memType")
            , DalbitUtil.convertRequestParamToString(request,"memId")
            , DalbitUtil.convertRequestParamToString(request,"memPwd")
            , DalbitUtil.convertRequestParamToString(request,"gender")
            , DalbitUtil.convertRequestParamToString(request,"nickNm")
            , DalbitUtil.convertRequestParamToInteger(request,"birthYY")
            , DalbitUtil.convertRequestParamToInteger(request,"birthMM")
            , DalbitUtil.convertRequestParamToInteger(request,"birthDD")
            , DalbitUtil.convertRequestParamToString(request,"term1")
            , DalbitUtil.convertRequestParamToString(request,"term2")
            , DalbitUtil.convertRequestParamToString(request,"term3")
            , DalbitUtil.convertRequestParamToString(request,"name")
            , DalbitUtil.convertRequestParamToString(request,"profImg")
            , DalbitUtil.convertRequestParamToInteger(request,"profImgRacy")
            , DalbitUtil.convertRequestParamToString(request,"email")
            , DalbitUtil.convertRequestParamToInteger(request,"os")
            , DalbitUtil.convertRequestParamToString(request,"deviceId")
            , DalbitUtil.convertRequestParamToString(request,"deviceToken")
            , DalbitUtil.convertRequestParamToString(request,"appVer")
            , DalbitUtil.convertRequestParamToString(request,"appAdId")
        );

        String result = memberService.signup(joinVo);
        return result;
    }

    @GetMapping("nick")
    public String nick(HttpServletRequest request){

        String nickName = DalbitUtil.convertRequestParamToString(request,"s_nickNm");
        if(nickName.equals("")){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임_파라메터오류));
        }

        return memberService.callNickNameCheck(new ProcedureVo(nickName));
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping("/pwd")
    public String pwd(HttpServletRequest request){

        P_ChangePasswordVo apiData = P_ChangePasswordVo.builder()
                ._phoneNo(DalbitUtil.convertRequestParamToString(request,"s_phoneNo"))
                ._password(DalbitUtil.convertRequestParamToString(request,"s_pwd"))
                .build();
        String result = memberService.callChangePassword(apiData);

        log.info("result: {}", result);
        return result;
    }

}

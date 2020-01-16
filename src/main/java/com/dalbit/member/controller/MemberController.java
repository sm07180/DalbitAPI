package com.dalbit.member.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "회원가입")
    @PostMapping("signup")
    public String signup(HttpServletRequest request){

        P_JoinVo joinVo = P_JoinVo.builder()
            .memSlct(DalbitUtil.convertRequestParamToString(request,"s_mem"))
            .id(DalbitUtil.convertRequestParamToString(request,"s_id"))
            .pw(DalbitUtil.convertRequestParamToString(request,"s_pwd"))
            .memSex(DalbitUtil.convertRequestParamToString(request,"s_gender"))
            .nickName(DalbitUtil.convertRequestParamToString(request,"s_nickNm"))
            .birthYear(DalbitUtil.convertRequestParamToInteger(request,"i_birthYY"))
            .birthMonth(DalbitUtil.convertRequestParamToInteger(request,"i_birthMM"))
            .birthDay(DalbitUtil.convertRequestParamToInteger(request,"i_birthDD"))
            .terms1(DalbitUtil.convertRequestParamToString(request,"s_term1"))
            .terms2(DalbitUtil.convertRequestParamToString(request,"s_term2"))
            .terms3(DalbitUtil.convertRequestParamToString(request,"s_term3"))
            .name(DalbitUtil.convertRequestParamToString(request,"s_name"))
            .profileImage(DalbitUtil.convertRequestParamToString(request,"s_profImg"))
            .email(DalbitUtil.convertRequestParamToString(request,"s_email"))
            .os(DalbitUtil.convertRequestParamToInteger(request,"i_os"))
            .deviceUuid(DalbitUtil.convertRequestParamToString(request,"s_deviceId"))
            .deviceToken(DalbitUtil.convertRequestParamToString(request,"s_deviceToken"))
            .appVersion(DalbitUtil.convertRequestParamToString(request,"s_appVer"))
            .build();

        String result = memberService.signup(joinVo);
        return result;
    }

    @ApiOperation(value = "닉네임 중복체크")
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
    @ApiOperation(value = "비밀번호 변경")
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

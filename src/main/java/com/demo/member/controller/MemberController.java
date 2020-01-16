package com.demo.member.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.member.service.MemberService;
import com.demo.member.vo.*;
import com.demo.util.DalbitUtil;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
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

    /**
     * 프로필편집
     */
    @ApiOperation(value = "프로필편집")
    @PostMapping("/profile")
    public String editProfile(HttpServletRequest request){
        P_ProfileEditVo apiData = P_ProfileEditVo.builder()
                .memSex(DalbitUtil.convertRequestParamToString(request,"s_gender"))
                .nickName(DalbitUtil.convertRequestParamToString(request,"s_nickNm"))
                .name(DalbitUtil.convertRequestParamToString(request,"s_name"))
                .birthYear(DalbitUtil.convertRequestParamToInteger(request,"i_birthYY"))
                .birthMonth(DalbitUtil.convertRequestParamToInteger(request,"i_birthMM"))
                .birthDay(DalbitUtil.convertRequestParamToInteger(request,"i_birthDD"))
                .profileImage(DalbitUtil.convertRequestParamToString(request,"s_profImg"))
                .profileImage(DalbitUtil.convertRequestParamToString(request,"s_bgImg"))
                .profileMsg(DalbitUtil.convertRequestParamToString(request,"s_message"))
                .build();

        log.info("playToken: {}", apiData.getName());

        String result = memberService.callProfileEdit(apiData);
        return result;
    }

    /**
     * 팬가입
     */
    @ApiOperation(value = "팬가입")
    @PostMapping("/fan")
    public String fanstarInsert(HttpServletRequest request){
        //참가를 위한 토큰 받기
        P_FanstarInsertVo apiData = P_FanstarInsertVo.builder()
                .fanMemNo(MemberVo.getUserInfo().getMem_no())
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = memberService.callFanstarInsert(apiData);

        return result;
    }

    /**
     * 팬해제
     */
    @ApiOperation(value = "팬 해제")
    @DeleteMapping("/fan")
    public String fanstarDelete(HttpServletRequest request){
        //참가를 위한 토큰 받기
        P_FanstarDeleteVo apiData = P_FanstarDeleteVo.builder()
                .fanMemNo(MemberVo.getUserInfo().getMem_no())
                .starMemNo(DalbitUtil.convertRequestParamToString(request,"s_mem_no"))
                .build();

        String result = memberService.callFanstarDelete(apiData);

        return result;
    }
}

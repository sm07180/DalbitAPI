package com.demo.member.controller;

import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.GlobalException;
import com.demo.member.service.MemberService;
import com.demo.member.vo.MemberVo;
import com.demo.member.vo.P_InfoVo;
import com.demo.member.vo.P_JoinVo;
import com.demo.util.CommonUtil;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.spring.web.json.Json;

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
            .memSlct(StringUtil.convertRequestParamToString(request,"s_mem"))
            .id(StringUtil.convertRequestParamToString(request,"s_id"))
            .pw(StringUtil.convertRequestParamToString(request,"s_pwd"))
            .memSex(StringUtil.convertRequestParamToString(request,"s_gender"))
            .nickName(StringUtil.convertRequestParamToString(request,"s_nickNm"))
            .birthYear(StringUtil.convertRequestParamToInteger(request,"i_birthYY"))
            .birthMonth(StringUtil.convertRequestParamToInteger(request,"i_birthMM"))
            .birthDay(StringUtil.convertRequestParamToInteger(request,"i_birthDD"))
            .terms1(StringUtil.convertRequestParamToString(request,"s_term1"))
            .terms2(StringUtil.convertRequestParamToString(request,"s_term2"))
            .terms3(StringUtil.convertRequestParamToString(request,"s_term3"))
            .name(StringUtil.convertRequestParamToString(request,"s_name"))
            .profileImage(StringUtil.convertRequestParamToString(request,"s_profImg"))
            .email(StringUtil.convertRequestParamToString(request,"s_email"))
            .os(StringUtil.convertRequestParamToInteger(request,"i_os"))
            .deviceUuid(StringUtil.convertRequestParamToString(request,"s_deviceId"))
            .deviceToken(StringUtil.convertRequestParamToString(request,"s_deviceToken"))
            .appVersion(StringUtil.convertRequestParamToString(request,"s_appVer"))
            .build();

        String result = memberService.signup(joinVo);
        return result;
    }

    @ApiOperation(value = "닉네임 중복체크")
    @GetMapping("nick")
    public String nick(HttpServletRequest request){

        String nickName = StringUtil.convertRequestParamToString(request,"s_nickNm");
        if(nickName.equals("")){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임_파라메터오류));
        }

        return memberService.callNickNameCheck(new ProcedureVo(nickName));
    }

    @ApiOperation(value = "회원정보 조회")
    @GetMapping("mypage")
    public String mypage() throws GlobalException {

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(MemberVo.getUserInfo().getMem_no())
                .build();

        String result = memberService.getMemberInfo(apiData);

        return result;
    }

    /**
     * 회원정보 조회
     */
    @ApiOperation(value = "회원정보 조회")
    @GetMapping("profile")
    public String profile(HttpServletRequest request) throws GlobalException{

        P_InfoVo apiData = P_InfoVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .target_mem_no(StringUtil.convertRequestParamToString(request, "s_mem_no"))
                .build();

        String result = memberService.getMemberInfo(apiData);

        return result;
    }
}

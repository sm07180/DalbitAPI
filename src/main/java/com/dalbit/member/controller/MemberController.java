package com.dalbit.member.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.vo.*;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    LoginUtil loginUtil;

    /**
     * 로그인 토큰생성
     */
    @GetMapping("token")
    public String token(HttpServletRequest request){

        TokenVo tokenVo;
        boolean isLogin = DalbitUtil.isLogin();

        if(isLogin){
            tokenVo = new TokenVo(jwtUtil.generateToken(MemberVo.getMyMemNo(), isLogin), MemberVo.getMyMemNo(), isLogin);
        }else{
            P_LoginVo pLoginVo = new P_LoginVo(
                //DalbitUtil.convertRequestParamToString(request,"memType")
                "a"
                , DalbitUtil.convertRequestParamToInteger(request,"os")
                , DalbitUtil.convertRequestParamToString(request,"deviceId")
                , DalbitUtil.convertRequestParamToString(request,"deviceToken")
                , DalbitUtil.convertRequestParamToString(request,"appVer")
                , DalbitUtil.convertRequestParamToString(request,"appAdId")
            );

            ProcedureVo procedureVo = memberService.callMemberLogin(pLoginVo);
            if(procedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요));

            }else if(procedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_패스워드틀림));

            }else if(procedureVo.getRet().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
                return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_파라메터이상));
            }

            HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String memNo = DalbitUtil.getStringMap(map,"mem_no");
            tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);

            memberService.refreshAnonymousSecuritySession(memNo);
        }



        return gsonUtil.toJson(new JsonOutputVo(Status.조회, tokenVo));
    }

    /**
     * 회원가입
     */
    @PostMapping("signup")
    public String signup(HttpServletRequest request, HttpServletResponse response) throws GlobalException {

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

        String result = "";
        ProcedureVo procedureVo = memberService.signup(joinVo);
        if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
            //new JsonOutputVo(Status.회원가입성공);

            HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String memNo = DalbitUtil.getStringMap(map, "mem_no");
            String jwtToken = jwtUtil.generateToken(memNo, true);

            UserDetails userDetails = userDetailsService.loadUserBySsoCookieFromDb(memNo);

            loginUtil.saveSecuritySession(request, userDetails);
            loginUtil.ssoCookieRenerate(response, jwtToken);

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공, new TokenVo(jwtToken, memNo, true)));

        }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입, procedureVo.getData()));

        }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복, procedureVo.getData()));

        }else if (Status.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류, procedureVo.getData()));

        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류, procedureVo.getData()));
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     */
    @GetMapping("nick")
    public String nick(HttpServletRequest request){

        String _data = DalbitUtil.convertRequestParamToString(request,"nickNm");
        if(_data.equals("")){
            return gsonUtil.toJson(new JsonOutputVo(Status.닉네임_파라메터오류));
        }

        return memberService.callNickNameCheck(new ProcedureVo(_data));
    }



    /**
     * 비밀번호 변경
     */
    @PostMapping("/pwd")
    public String pwd(HttpServletRequest request){

        P_ChangePasswordVo pChangePasswordVo = new P_ChangePasswordVo();
        pChangePasswordVo.set_phoneNo(DalbitUtil.convertRequestParamToString(request, "memId"));
        pChangePasswordVo.set_password(DalbitUtil.convertRequestParamToString(request, "memPwd"));


        String result = memberService.callChangePassword(pChangePasswordVo);

        log.info("result: {}", result);
        return result;
    }

}

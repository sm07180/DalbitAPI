package com.dalbit.member.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.member.vo.procedure.P_ChangePasswordVo;
import com.dalbit.member.vo.procedure.P_JoinVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.member.vo.request.ChangePwVo;
import com.dalbit.member.vo.request.SignUpVo;
import com.dalbit.member.vo.request.NickNmDupleCheckVo;
import com.dalbit.sample.service.SampleService;
import com.dalbit.sample.vo.SampleVo;
import com.dalbit.security.service.UserDetailsServiceImpl;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    ProfileService profileService;
    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    LoginUtil loginUtil;
    @Autowired
    SampleService sampleService;
    @Autowired
    CommonService commonService;

    /**
     * 토큰조회
     */
    @GetMapping("token")
    public String token(HttpServletRequest request){
        HashMap<String, Object> result = commonService.getJwtTokenInfo(request);

        if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요));

        }else if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_패스워드틀림));

        }else if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_파라메터이상));
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result.get("tokenVo")));
    }

    /**
     * 회원가입
     */
    @PostMapping("member/signup")
    public String signup(@Valid SignUpVo signUpVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        String memType = signUpVo.getMemType();
        String memId = signUpVo.getMemId();
        String memPwd = signUpVo.getMemPwd();
        String profImg = signUpVo.getProfImg();

        if(DalbitUtil.isEmpty(profImg)){
            profImg = "/profile_3/profile_"+ signUpVo.getGender()+".jpg";
        }else{
            profImg = DalbitUtil.replacePath(profImg);
        }
        signUpVo.setProfImg(profImg);

        DeviceVo deviceVo = new DeviceVo(request);
        int os = deviceVo.getOs();
        String deviceId = deviceVo.getDeviceUuid();
        String deviceToken = deviceVo.getDeviceToken();
        String appVer = deviceVo.getAppVersion();
        String appAdId = deviceVo.getAdId();
        String ip = deviceVo.getIp();

        LocationVo locationVo = DalbitUtil.getLocation(deviceVo.getIp());

        P_JoinVo joinVo = new P_JoinVo(
                signUpVo
                , os
                , deviceId
                , deviceToken
                , appVer
                , appAdId
                , locationVo.getRegionName()
                , ip
        );

        String result = "";
        ProcedureVo procedureVo = memberService.signup(joinVo);
        if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){

            //로그인 처리
            P_LoginVo pLoginVo = new P_LoginVo(memType, memId, memPwd, os, deviceId, deviceToken, appVer, appAdId, locationVo.getRegionName(), ip);

            memberService.callMemberLogin(pLoginVo);
            log.debug("로그인 결과 : {}", new Gson().toJson(procedureVo));

            HashMap map = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String memNo = DalbitUtil.getStringMap(map, "mem_no");
            String jwtToken = jwtUtil.generateToken(memNo, true);

            ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, memNo));

            MemberVo memberVo = null;
            if(profileProcedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {

                P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
                memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, memNo, null));
                memberVo.setMemSlct(memType);
                memberVo.setMemPasswd(memPwd);
            }else{
                new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
            }

            SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemPasswd(), DalbitUtil.getAuthorities());
            securityUserVo.setMemberVo(memberVo);

            loginUtil.saveSecuritySession(request, securityUserVo);
            //loginUtil.ssoCookieRenerate(response, jwtToken);

            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공, new TokenVo(jwtToken, memNo, true)));

        }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입));
        }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복));
        }else if (Status.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
            result = gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류));
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     */
    @GetMapping("member/nick")
    public String nick(@Valid NickNmDupleCheckVo nickNmDupleCheckVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        String result = memberService.callNickNameCheck(new ProcedureVo(nickNmDupleCheckVo.getNickNm()));

        return result;
    }



    /**
     * 비밀번호 변경
     */
    @PostMapping("member/pwd")
    public String pwd(@Valid ChangePwVo changePwVo, BindingResult bindingResult) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult);

        P_ChangePasswordVo pChangePasswordVo = new P_ChangePasswordVo();
        pChangePasswordVo.setPhoneNo(changePwVo.getMemId());
        pChangePasswordVo.setPassword(changePwVo.getMemPwd());

        String result = memberService.callChangePassword(pChangePasswordVo);

        return result;
    }

    /**
     * ID 리스트 가져오기 (임시 테스트용)
     */
    @GetMapping("id")
    @Profile({"local", "dev"})
    public List<SampleVo> selectMemid(){

        List<SampleVo> idList = sampleService.selectMemId();

        return idList;
    }

}

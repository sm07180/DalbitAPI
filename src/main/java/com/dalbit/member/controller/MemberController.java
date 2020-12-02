package com.dalbit.member.controller;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.AdbrixService;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.CustomUsernameNotFoundException;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.ProfileInfoOutVo;
import com.dalbit.member.vo.TokenVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.sample.service.SampleService;
import com.dalbit.security.vo.SecurityUserVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

@Slf4j
@RestController
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    ProfileService profileService;
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

    @Autowired
    AdbrixService adbrixService;
    @Autowired
    MemberDao memberDao;

    /**
     * 토큰조회
     */
    @GetMapping("/token")
    public String token(HttpServletRequest request){
        HashMap<String, Object> result = null;
        try{
            result = commonService.getJwtTokenInfo(request);
        }catch (Exception e){
            //비회원
            LocationVo locationVo = null;
            String browser = DalbitUtil.getUserAgent(request);
            DeviceVo deviceVo = new DeviceVo(request) ;
            String dbSelectMemNo = "88888888888888";
            boolean isLogin = false;
            P_LoginVo pLoginVo = new P_LoginVo("a", deviceVo.getOs(), deviceVo.getDeviceUuid(), deviceVo.getDeviceToken(), deviceVo.getAppVersion(), deviceVo.getAdId(), locationVo == null ? "" : locationVo.getRegionName(), deviceVo.getIp(), browser);
            memberService.callMemberLogin(pLoginVo);

            TokenVo tokenVo = new TokenVo(jwtUtil.generateToken(dbSelectMemNo, isLogin), dbSelectMemNo, isLogin);
            memberService.refreshAnonymousSecuritySession(dbSelectMemNo);

            log.info("#### OverStack ##### tokenVo: {}", tokenVo);
            return gsonUtil.toJson(new JsonOutputVo(Status.조회, tokenVo));

        }

        if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_회원가입필요.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요));

        }else if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_패스워드틀림.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_패스워드틀림));

        }else if(((Status)result.get("Status")).getMessageCode().equals(Status.로그인실패_파라메터이상.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_파라메터이상));
        }
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, result.get("tokenVo")));
    }

    @GetMapping("/token/short")
    public String tokenShort(HttpServletRequest request) throws GlobalException{
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, jwtUtil.getTokenVoFromJwt(jwtUtil.generateToken(MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request), 1000 * 60 * 5))));
    }

    /**
     * 회원가입
     */
    @PostMapping("/member/signup")
    public String signup(@Valid SignUpVo signUpVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String memType = signUpVo.getMemType();
        String memId = signUpVo.getMemId();
        String memPwd = signUpVo.getMemPwd();
        String profImg = signUpVo.getProfImg();

        if(DalbitUtil.isEmpty(profImg) || profImg.startsWith(Code.포토_프로필_디폴트_PREFIX.getCode())){
            profImg = "";
        }
        signUpVo.setProfImg(profImg);

        DeviceVo deviceVo = new DeviceVo(request);
        int os = deviceVo.getOs();
        String deviceId = deviceVo.getDeviceUuid();
        String deviceToken = deviceVo.getDeviceToken();
        String appVer = deviceVo.getAppVersion();
        String appAdId = deviceVo.getAdId();
        String ip = deviceVo.getIp();
        String browser = DalbitUtil.getUserAgent(request);
        String appBuild = deviceVo.getAppBuild();
        String nativeTid = DalbitUtil.isEmpty(signUpVo.getNativeTid()) ? "" : signUpVo.getNativeTid();

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
                , browser
                , nativeTid
        );

        String result = "";
        //log.debug("휴대폰번호{} - 세션 = |{}| / 인풋 = |{}|", memType, request.getSession().getAttribute("phoneNo").toString(), memId);
        //String s_phoneNo = (String)request.getSession().getAttribute("phoneNo");

        //서버호출번호가 간혹 변경되므로 휴대폰 번호 쿠키로 대체...

        CookieUtil cookieUtil = new CookieUtil(request);
        Cookie smsCookie = cookieUtil.getCookie("smsCookie");
        String s_phoneNo = "";
        if (DalbitUtil.isEmpty(smsCookie) && "p".equals(signUpVo.getMemType())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청_유효하지않은번호));
        }

        if("p".equals(signUpVo.getMemType())) {
            HashMap cookieResMap = new Gson().fromJson(URLDecoder.decode(smsCookie.getValue()), HashMap.class);
            s_phoneNo = (String) cookieResMap.get("phoneNo");
            s_phoneNo = DalbitUtil.isEmpty(s_phoneNo) ? "" : s_phoneNo.replaceAll("-", "");
        }

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(memId)){
            return gsonUtil.toJson(new JsonOutputVo(Status.부적절한문자열));
        }

        //어드민 block 상태 체크
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo));
        if(0 < adminBlockCnt){
            return gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_운영자차단));
        }

        if(!"p".equals(memType) || ("p".equals(memType) && s_phoneNo.equals(memId))){
            ProcedureVo procedureVo = memberService.signup(joinVo, request);
            //휴대폰 인증했던 번호와 일치여부 확인

            if(Status.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
                //로그인 처리
                P_LoginVo pLoginVo = new P_LoginVo(memType, memId, memPwd, os, deviceId, deviceToken, appVer, appAdId, locationVo.getRegionName(), ip, browser, appBuild);
                ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
                log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));

                HashMap map = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
                String memNo = DalbitUtil.getStringMap(map, "mem_no");
                String jwtToken = jwtUtil.generateToken(memNo, true);

                ProcedureVo profileProcedureVo = profileService.getProfile(new P_ProfileInfoVo(1, memNo));

                MemberVo memberVo = null;
                if(profileProcedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {

                    P_ProfileInfoVo profileInfo = new Gson().fromJson(profileProcedureVo.getExt(), P_ProfileInfoVo.class);
                    memberVo = new MemberVo(new ProfileInfoOutVo(profileInfo, memNo, memNo, null));
                    memberVo.setMemSlct(memType);
                    memberVo.setMemPasswd(memPwd);
                }else{
                    new CustomUsernameNotFoundException(Status.로그인실패_패스워드틀림);
                }

                SecurityUserVo securityUserVo = new SecurityUserVo(memberVo.getMemId(), memberVo.getMemPasswd(), DalbitUtil.getAuthorities());
                securityUserVo.setMemberVo(memberVo);

                loginUtil.saveSecuritySession(request, securityUserVo);
                //loginUtil.ssoCookieRenerate(response, jwtToken);

                //애드브릭스 데이터 전달을 위한 정보 생성
                AdbrixLayoutVo adbrixData = adbrixService.makeAdbrixData("signUp", memNo);

                var resultMap = new HashMap();
                resultMap.put("tokenInfo", new TokenVo(jwtToken, memNo, true));
                resultMap.put("adbrixData", adbrixData);

                //result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공, new TokenVo(jwtToken, memNo, true)));
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입성공, resultMap));
                try {
                    response.addCookie(CookieUtil.deleteCookie("smsCookie", "", "/", 0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (Status.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입));
            }else if (Status.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_닉네임중복));
            }else if (Status.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.파라미터오류));
            }else if (Status.회원가입실패_탈퇴회원.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_탈퇴회원));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입오류));
            }
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청_유효하지않은번호));
        }

        return result;
    }

    /**
     * 닉네임 중복체크
     */
    @GetMapping("/member/nick")
    public String nick(@Valid NickNmDupleCheckVo nickNmDupleCheckVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = memberService.callNickNameCheck(nickNmDupleCheckVo.getNickNm().trim());

        return result;
    }


    /**
     * 비밀번호 변경
     */
    @PostMapping("/member/pwd")
    public String pwd(@Valid ChangePwVo changePwVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ChangePasswordVo pChangePasswordVo = new P_ChangePasswordVo();
        pChangePasswordVo.setPhoneNo(changePwVo.getMemId());
        pChangePasswordVo.setPassword(changePwVo.getMemPwd());

        String result = memberService.callChangePassword(request, pChangePasswordVo);

        return result;
    }


    /**
     * 회원탈퇴
     */
    @PostMapping("/member/withdrawal")
    public String memberWithdrawal(@Valid WithdrawalVo withdrawalVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_WithdrawalVo apiData = new P_WithdrawalVo(withdrawalVo, request);
        String result = memberService.callMemberWithdrawal(apiData);
        return result;
    }


    /**
     * 회원 환전 계산
     */
    @PostMapping("/member/exchange/calc")
    public String exchangeCalc(@Valid ExchangeVo exchangeVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ExchangeVo apiData = new P_ExchangeVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setByeol(exchangeVo.getByeol());

        String result = memberService.callExchangeCalc(apiData);
        return result;
    }

    /**
     * 회원 환전 신청
     */
    @PostMapping("/member/exchange/apply")
    public String exchangeApply(@Valid ExchangeApplyVo exchangeApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ExchangeApplyVo apiData = new P_ExchangeApplyVo(exchangeApplyVo, request);

        String result = memberService.callExchangeApply(apiData, request);
        return result;
    }

    /**
     * 회원 환전 승인 건 조회
     */
    @PostMapping("/member/exchange/select")
    public String exchangeApprovalSelect(HttpServletRequest request) throws GlobalException{

        String result = memberService.exchangeApprovalSelect(MemberVo.getMyMemNo(request));
        return result;
    }


    /**
     * 환전 재신청 (기존정보)
     */
    @PostMapping("/member/exchange/reapply")
    public String exchangeReapply(@Valid ExchangeReApplyVo exchangeReApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //벨리데이션 체크
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = memberService.exchangeReapply(exchangeReApplyVo, request);
        return result;
    }

    @PostMapping("/member/reset/listen")
    public String changeDevice(HttpServletRequest request) throws GlobalException {
        return memberService.resetListeningRoom(request);
    }


    /**
     * 환전 계좌 등록
     */
    @PostMapping("/member/exchange/account/add")
    public String accountAdd(@Valid ExchangeAccountAddVo exchangeAccountInfoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountAddVo apiData = new P_ExchangeAccountAddVo(exchangeAccountInfoVo, request);
        String result = memberService.callAccountAdd(apiData);
        return result;
    }


    /**
     * 환전 계좌 수정
     */
    @PostMapping("/member/exchange/account/edit")
    public String accountEdit(@Valid ExchangeAccountEditVo exchangeAccountEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountEditVo apiData = new P_ExchangeAccountEditVo(exchangeAccountEditVo, request);
        String result = memberService.callAccountEdit(apiData, request);
        return result;
    }


    /**
     * 환전 계좌 삭제
     */
    @PostMapping("/member/exchange/account/delete")
    public String accountDelete(@Valid ExchangeAccountDeleteVo exchangeAccountDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountDeleteVo apiData = new P_ExchangeAccountDeleteVo(exchangeAccountDeleteVo, request);
        String result = memberService.callAccountDelete(apiData, request);
        return result;
    }

    /**
     * 환전 계좌 조회
     */
    @GetMapping("/member/exchange/account/list")
    public String accountDelete(HttpServletRequest request) {
        P_ExchangeAccountListVo apiData = new P_ExchangeAccountListVo(request);
        String result = memberService.callAccountListSelect(apiData, "");
        return result;
    }

    /**
     * 스페셜DJ 선정 이력(약력) 히스토리
     */
    @GetMapping("/member/special/history")
    public String getSpecialHistory(@Valid SpecialDjHistoryVo specialDjHistoryVo, HttpServletRequest request){
        P_SpecialDjHistoryVo apiData = new P_SpecialDjHistoryVo(specialDjHistoryVo);
        String result = memberService.getSpecialHistory(apiData, request);
        return result;
    }


    /**
     * 스페셜DJ 선발 누적 가산점 조회
     */
    @GetMapping("/member/special/point/list")
    public String getSpecialPointList(@Valid SpecialPointListVo specialPointListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_SpecialPointListVo apiData = new P_SpecialPointListVo(specialPointListVo);
        String result = memberService.getSpecialPointList(apiData);
        return result;
    }
}

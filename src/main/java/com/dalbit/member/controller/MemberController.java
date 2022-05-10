package com.dalbit.member.controller;

import com.dalbit.common.code.*;
import com.dalbit.common.service.AdbrixService;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.vo.request.SpecialHistoryVo;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
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
import org.springframework.context.annotation.Scope;
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
@Scope("prototype")
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
            boolean isLogin = false;
            P_LoginVo pLoginVo = new P_LoginVo("a", deviceVo.getOs(), deviceVo.getDeviceUuid(), deviceVo.getDeviceToken(), deviceVo.getAppVersion(), deviceVo.getAdId(), locationVo == null ? "" : locationVo.getRegionName(), deviceVo.getIp(), browser);
            ProcedureOutputVo loginProcedureVo = memberService.callMemberLogin(pLoginVo);
            if (loginProcedureVo.getRet().equals(MemberStatus.로그인성공.getMessageCode())) {
                HashMap map = new Gson().fromJson(loginProcedureVo.getExt(), HashMap.class);
                String memNo = DalbitUtil.getStringMap(map, "mem_no");

                TokenVo tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);
                memberService.refreshAnonymousSecuritySession(memNo);

                log.info("#### OverStack ##### tokenVo: {}", tokenVo);
                return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, tokenVo));
            }else{
                return gsonUtil.toJson(new JsonOutputVo(MemberStatus.로그인실패_파라메터이상));
            }
        }

        if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.로그인실패_회원가입필요.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.로그인실패_회원가입필요));

        }else if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.로그인실패_패스워드틀림.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.로그인실패_패스워드틀림));

        }else if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.로그인실패_파라메터이상.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.로그인실패_파라메터이상));
        }
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.조회, result.get("tokenVo")));
    }

    @GetMapping("/token/short")
    public String tokenShort(HttpServletRequest request) throws GlobalException{
        log.error("{}", TestStatus.플레이리스트_수정_실패);
        boolean login = DalbitUtil.isLogin(request);
        String myMemNo = MemberVo.getMyMemNo(request);
        String jwt = jwtUtil.generateToken(myMemNo, login, 1000L * 60L * 5L);
        TokenVo tokenVoFromJwt = jwtUtil.getTokenVoFromJwt(jwt);
        JsonOutputVo jsonOutputVo = new JsonOutputVo(CommonStatus.조회, tokenVoFromJwt);
        return gsonUtil.toJson(jsonOutputVo);
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
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.인증번호요청_유효하지않은번호));
        }

        if("p".equals(signUpVo.getMemType())) {
            HashMap cookieResMap = new Gson().fromJson(URLDecoder.decode(smsCookie.getValue()), HashMap.class);
            s_phoneNo = (String) cookieResMap.get("phoneNo");
            s_phoneNo = DalbitUtil.isEmpty(s_phoneNo) ? "" : s_phoneNo.replaceAll("-", "");
        }

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(memId)){
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.부적절한문자열));
        }

        //어드민 block 상태 체크
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo));
        if(0 < adminBlockCnt){
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.로그인실패_운영자차단));
        }

        if(!"p".equals(memType) || ("p".equals(memType) && s_phoneNo.equals(memId))){
            ProcedureVo procedureVo = memberService.signup(joinVo, request);
            //휴대폰 인증했던 번호와 일치여부 확인

            if(MemberStatus.회원가입성공.getMessageCode().equals(procedureVo.getRet())){
                //로그인 처리
                P_LoginVo pLoginVo = new P_LoginVo(memType, memId, memPwd, os, deviceId, deviceToken, appVer, appAdId, locationVo.getRegionName(), ip, browser, appBuild);
                ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
                log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));

                HashMap map = new Gson().fromJson(LoginProcedureVo.getExt(), HashMap.class);
                String memNo = DalbitUtil.getStringMap(map, "mem_no");
                String jwtToken = jwtUtil.generateToken(memNo, true);

                MemberVo memberVo = new MemberVo();
                memberVo.setMemId(signUpVo.getMemId());
                memberVo.setMemPasswd(signUpVo.getMemId());
                memberVo.setMemSlct(signUpVo.getMemType());

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
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.회원가입성공, resultMap));
                try {
                    response.addCookie(CookieUtil.deleteCookie("smsCookie", "", "/", 0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (MemberStatus.회원가입실패_중복가입.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.회원가입실패_중복가입));
            }else if (MemberStatus.회원가입실패_닉네임중복.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.회원가입실패_닉네임중복));
            }else if (MemberStatus.회원가입실패_파라메터오류.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.파라미터오류));
            }else if (MemberStatus.회원가입실패_탈퇴회원.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.회원가입실패_탈퇴회원));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.회원가입오류));
            }
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.인증번호요청_유효하지않은번호));
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


    /**
     * 랭킹데이터 반영 ON/OFF
     */
    @PostMapping("/member/rank/setting")
    public String rankSetting(@Valid MemberRankSettingVo memberRankSettingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberRankSettingVo apiData = new P_MemberRankSettingVo(memberRankSettingVo, request);
        String result = memberService.callRankSetting(apiData);
        return result;
    }


    /**
     * 회원 알림받기 등록/해제
     */
    @PostMapping("/member/recv")
    public String recvEdit(@Valid MemberReceiveVo memberReceiveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberReceiveVo apiData = new P_MemberReceiveVo(memberReceiveVo, request);
        String result = memberService.callRecvEdit(apiData);
        return result;
    }


    /**
     * 회원 알림받기 삭제
     */
    @PostMapping("/member/recv/delete")
    public String recvDelete(@Valid MemberReceiveDeleteVo memberReceiveDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberReceiveDeleteVo apiData = new P_MemberReceiveDeleteVo(memberReceiveDeleteVo, request);
        String result = memberService.callRecvDelete(apiData);
        return result;
    }


    /**
     * 알림받기 회원 조회
     */
    @GetMapping("/member/recv")
    public String recvList(HttpServletRequest request) throws GlobalException{
        P_MemberReceiveListVo apiData = new P_MemberReceiveListVo(request);
        String result = memberService.callRecvList(apiData);
        return result;
    }


    /**
     * 추천 DJ 목록 조회
     */
    @GetMapping("/dj/recommend")
    public String djRecommendList(@Valid DjRecommendListVo djRecommendListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_DjRecommendListVo apiData = new P_DjRecommendListVo(djRecommendListVo, request);
        String result = memberService.callDjRecommendList(apiData);
        return result;
    }


    /**
     * 회원 이미지 신고
     */
    @PostMapping("/report/image")
    public String reportImage(@Valid ReportImageVo reportImageVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ReportImageVo apiData = new P_ReportImageVo(reportImageVo, request);
        String result = memberService.callReportImage(apiData);
        return result;
    }

    /**
     * 베스트 DJ들의 팬랭킹 (1,2,3위) 리스트
     */
    @GetMapping("/dj/best/fan/rank/list")
    public String djBestFanRankList(@Valid SpecialHistoryVo specialHistoryVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return memberService.callDjBestFanRankList(specialHistoryVo, request);
    }
}

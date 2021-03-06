package com.dalbit.member.controller;

import com.dalbit.common.code.Code;
import com.dalbit.common.code.CommonStatus;
import com.dalbit.common.code.MemberStatus;
import com.dalbit.common.code.Status;
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
     * ํ ํฐ์กฐํ
     */
    @GetMapping("/token")
    public String token(HttpServletRequest request){
        HashMap<String, Object> result = null;
        try{
            result = commonService.getJwtTokenInfo(request);
        }catch (Exception e){
            //๋นํ์
            LocationVo locationVo = null;
            String browser = DalbitUtil.getUserAgent(request);
            DeviceVo deviceVo = new DeviceVo(request) ;
            boolean isLogin = false;
            P_LoginVo pLoginVo = new P_LoginVo("a", deviceVo.getOs(), deviceVo.getDeviceUuid(), deviceVo.getDeviceToken(), deviceVo.getAppVersion(), deviceVo.getAdId(), locationVo == null ? "" : locationVo.getRegionName(), deviceVo.getIp(), browser);
            ProcedureOutputVo loginProcedureVo = memberService.callMemberLogin(pLoginVo);
            if (loginProcedureVo.getRet().equals(MemberStatus.๋ก๊ทธ์ธ์ฑ๊ณต.getMessageCode())) {
                HashMap map = new Gson().fromJson(loginProcedureVo.getExt(), HashMap.class);
                String memNo = DalbitUtil.getStringMap(map, "mem_no");

                TokenVo tokenVo = new TokenVo(jwtUtil.generateToken(memNo, isLogin), memNo, isLogin);
                memberService.refreshAnonymousSecuritySession(memNo);

                log.info("#### OverStack ##### tokenVo: {}", tokenVo);
                return gsonUtil.toJson(new JsonOutputVo(CommonStatus.์กฐํ, tokenVo));
            }else{
                return gsonUtil.toJson(new JsonOutputVo(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํ๋ผ๋ฉํฐ์ด์));
            }
        }

        if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํ์๊ฐ์ํ์.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํ์๊ฐ์ํ์));

        }else if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํจ์ค์๋ํ๋ฆผ.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํจ์ค์๋ํ๋ฆผ));

        }else if(((Status)result.get("Status")).getMessageCode().equals(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํ๋ผ๋ฉํฐ์ด์.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.๋ก๊ทธ์ธ์คํจ_ํ๋ผ๋ฉํฐ์ด์));
        }
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.์กฐํ, result.get("tokenVo")));
    }

    @GetMapping("/token/short")
    public String tokenShort(HttpServletRequest request) throws GlobalException{
        return gsonUtil.toJson(new JsonOutputVo(CommonStatus.์กฐํ, jwtUtil.getTokenVoFromJwt(jwtUtil.generateToken(MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request), 1000 * 60 * 5))));
    }

    /**
     * ํ์๊ฐ์
     */
    @PostMapping("/member/signup")
    public String signup(@Valid SignUpVo signUpVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GlobalException{

        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String memType = signUpVo.getMemType();
        String memId = signUpVo.getMemId();
        String memPwd = signUpVo.getMemPwd();
        String profImg = signUpVo.getProfImg();

        if(DalbitUtil.isEmpty(profImg) || profImg.startsWith(Code.ํฌํ _ํ๋กํ_๋ํดํธ_PREFIX.getCode())){
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
        //log.debug("ํด๋ํฐ๋ฒํธ{} - ์ธ์ = |{}| / ์ธํ = |{}|", memType, request.getSession().getAttribute("phoneNo").toString(), memId);
        //String s_phoneNo = (String)request.getSession().getAttribute("phoneNo");

        //์๋ฒํธ์ถ๋ฒํธ๊ฐ ๊ฐํน ๋ณ๊ฒฝ๋๋ฏ๋ก ํด๋ํฐ ๋ฒํธ ์ฟ ํค๋ก ๋์ฒด...

        CookieUtil cookieUtil = new CookieUtil(request);
        Cookie smsCookie = cookieUtil.getCookie("smsCookie");
        String s_phoneNo = "";
        if (DalbitUtil.isEmpty(smsCookie) && "p".equals(signUpVo.getMemType())) {
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.์ธ์ฆ๋ฒํธ์์ฒญ_์ ํจํ์ง์์๋ฒํธ));
        }

        if("p".equals(signUpVo.getMemType())) {
            HashMap cookieResMap = new Gson().fromJson(URLDecoder.decode(smsCookie.getValue()), HashMap.class);
            s_phoneNo = (String) cookieResMap.get("phoneNo");
            s_phoneNo = DalbitUtil.isEmpty(s_phoneNo) ? "" : s_phoneNo.replaceAll("-", "");
        }

        // ๋ถ์ ์ ํ๋ฌธ์์ด ์ฒดํฌ ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(memId)){
            return gsonUtil.toJson(new JsonOutputVo(CommonStatus.๋ถ์ ์ ํ๋ฌธ์์ด));
        }

        //์ด๋๋ฏผ block ์ํ ์ฒดํฌ
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(deviceVo));
        if(0 < adminBlockCnt){
            return gsonUtil.toJson(new JsonOutputVo(MemberStatus.๋ก๊ทธ์ธ์คํจ_์ด์์์ฐจ๋จ));
        }

        if(!"p".equals(memType) || ("p".equals(memType) && s_phoneNo.equals(memId))){
            ProcedureVo procedureVo = memberService.signup(joinVo, request);
            //ํด๋ํฐ ์ธ์ฆํ๋ ๋ฒํธ์ ์ผ์น์ฌ๋ถ ํ์ธ

            if(MemberStatus.ํ์๊ฐ์์ฑ๊ณต.getMessageCode().equals(procedureVo.getRet())){
                //๋ก๊ทธ์ธ ์ฒ๋ฆฌ
                P_LoginVo pLoginVo = new P_LoginVo(memType, memId, memPwd, os, deviceId, deviceToken, appVer, appAdId, locationVo.getRegionName(), ip, browser, appBuild);
                ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
                log.debug("๋ก๊ทธ์ธ ๊ฒฐ๊ณผ : {}", new Gson().toJson(LoginProcedureVo));

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

                //์ ๋๋ธ๋ฆญ์ค ๋ฐ์ดํฐ ์ ๋ฌ์ ์ํ ์ ๋ณด ์์ฑ
                AdbrixLayoutVo adbrixData = adbrixService.makeAdbrixData("signUp", memNo);

                var resultMap = new HashMap();
                resultMap.put("tokenInfo", new TokenVo(jwtToken, memNo, true));
                resultMap.put("adbrixData", adbrixData);

                //result = gsonUtil.toJson(new JsonOutputVo(Status.ํ์๊ฐ์์ฑ๊ณต, new TokenVo(jwtToken, memNo, true)));
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.ํ์๊ฐ์์ฑ๊ณต, resultMap));
                try {
                    response.addCookie(CookieUtil.deleteCookie("smsCookie", "", "/", 0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (MemberStatus.ํ์๊ฐ์์คํจ_์ค๋ณต๊ฐ์.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.ํ์๊ฐ์์คํจ_์ค๋ณต๊ฐ์));
            }else if (MemberStatus.ํ์๊ฐ์์คํจ_๋๋ค์์ค๋ณต.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.ํ์๊ฐ์์คํจ_๋๋ค์์ค๋ณต));
            }else if (MemberStatus.ํ์๊ฐ์์คํจ_ํ๋ผ๋ฉํฐ์ค๋ฅ.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(CommonStatus.ํ๋ผ๋ฏธํฐ์ค๋ฅ));
            }else if (MemberStatus.ํ์๊ฐ์์คํจ_ํํดํ์.getMessageCode().equals(procedureVo.getRet())){
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.ํ์๊ฐ์์คํจ_ํํดํ์));
            }else{
                result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.ํ์๊ฐ์์ค๋ฅ));
            }
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(MemberStatus.์ธ์ฆ๋ฒํธ์์ฒญ_์ ํจํ์ง์์๋ฒํธ));
        }

        return result;
    }

    /**
     * ๋๋ค์ ์ค๋ณต์ฒดํฌ
     */
    @GetMapping("/member/nick")
    public String nick(@Valid NickNmDupleCheckVo nickNmDupleCheckVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = memberService.callNickNameCheck(nickNmDupleCheckVo.getNickNm().trim());

        return result;
    }


    /**
     * ๋น๋ฐ๋ฒํธ ๋ณ๊ฒฝ
     */
    @PostMapping("/member/pwd")
    public String pwd(@Valid ChangePwVo changePwVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ChangePasswordVo pChangePasswordVo = new P_ChangePasswordVo();
        pChangePasswordVo.setPhoneNo(changePwVo.getMemId());
        pChangePasswordVo.setPassword(changePwVo.getMemPwd());

        String result = memberService.callChangePassword(request, pChangePasswordVo);

        return result;
    }


    /**
     * ํ์ํํด
     */
    @PostMapping("/member/withdrawal")
    public String memberWithdrawal(@Valid WithdrawalVo withdrawalVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_WithdrawalVo apiData = new P_WithdrawalVo(withdrawalVo, request);
        String result = memberService.callMemberWithdrawal(apiData);
        return result;
    }


    /**
     * ํ์ ํ์  ๊ณ์ฐ
     */
    @PostMapping("/member/exchange/calc")
    public String exchangeCalc(@Valid ExchangeVo exchangeVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ExchangeVo apiData = new P_ExchangeVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setByeol(exchangeVo.getByeol());

        String result = memberService.callExchangeCalc(apiData);
        return result;
    }

    /**
     * ํ์ ํ์  ์ ์ฒญ
     */
    @PostMapping("/member/exchange/apply")
    public String exchangeApply(@Valid ExchangeApplyVo exchangeApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ExchangeApplyVo apiData = new P_ExchangeApplyVo(exchangeApplyVo, request);

        String result = memberService.callExchangeApply(apiData, request);
        return result;
    }

    /**
     * ํ์ ํ์  ์น์ธ ๊ฑด ์กฐํ
     */
    @PostMapping("/member/exchange/select")
    public String exchangeApprovalSelect(HttpServletRequest request) throws GlobalException{

        String result = memberService.exchangeApprovalSelect(MemberVo.getMyMemNo(request));
        return result;
    }


    /**
     * ํ์  ์ฌ์ ์ฒญ (๊ธฐ์กด์ ๋ณด)
     */
    @PostMapping("/member/exchange/reapply")
    public String exchangeReapply(@Valid ExchangeReApplyVo exchangeReApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        //๋ฒจ๋ฆฌ๋ฐ์ด์ ์ฒดํฌ
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = memberService.exchangeReapply(exchangeReApplyVo, request);
        return result;
    }

    @PostMapping("/member/reset/listen")
    public String changeDevice(HttpServletRequest request) throws GlobalException {
        return memberService.resetListeningRoom(request);
    }


    /**
     * ํ์  ๊ณ์ข ๋ฑ๋ก
     */
    @PostMapping("/member/exchange/account/add")
    public String accountAdd(@Valid ExchangeAccountAddVo exchangeAccountInfoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountAddVo apiData = new P_ExchangeAccountAddVo(exchangeAccountInfoVo, request);
        String result = memberService.callAccountAdd(apiData);
        return result;
    }


    /**
     * ํ์  ๊ณ์ข ์์ 
     */
    @PostMapping("/member/exchange/account/edit")
    public String accountEdit(@Valid ExchangeAccountEditVo exchangeAccountEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountEditVo apiData = new P_ExchangeAccountEditVo(exchangeAccountEditVo, request);
        String result = memberService.callAccountEdit(apiData, request);
        return result;
    }


    /**
     * ํ์  ๊ณ์ข ์ญ์ 
     */
    @PostMapping("/member/exchange/account/delete")
    public String accountDelete(@Valid ExchangeAccountDeleteVo exchangeAccountDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExchangeAccountDeleteVo apiData = new P_ExchangeAccountDeleteVo(exchangeAccountDeleteVo, request);
        String result = memberService.callAccountDelete(apiData, request);
        return result;
    }

    /**
     * ํ์  ๊ณ์ข ์กฐํ
     */
    @GetMapping("/member/exchange/account/list")
    public String accountDelete(HttpServletRequest request) {
        P_ExchangeAccountListVo apiData = new P_ExchangeAccountListVo(request);
        String result = memberService.callAccountListSelect(apiData, "");
        return result;
    }

    /**
     * ์คํ์DJ ์ ์  ์ด๋ ฅ(์ฝ๋ ฅ) ํ์คํ ๋ฆฌ
     */
    @GetMapping("/member/special/history")
    public String getSpecialHistory(@Valid SpecialDjHistoryVo specialDjHistoryVo, HttpServletRequest request){
        P_SpecialDjHistoryVo apiData = new P_SpecialDjHistoryVo(specialDjHistoryVo);
        String result = memberService.getSpecialHistory(apiData, request);
        return result;
    }


    /**
     * ์คํ์DJ ์ ๋ฐ ๋์  ๊ฐ์ฐ์  ์กฐํ
     */
    @GetMapping("/member/special/point/list")
    public String getSpecialPointList(@Valid SpecialPointListVo specialPointListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_SpecialPointListVo apiData = new P_SpecialPointListVo(specialPointListVo);
        String result = memberService.getSpecialPointList(apiData);
        return result;
    }


    /**
     * ๋ญํน๋ฐ์ดํฐ ๋ฐ์ ON/OFF
     */
    @PostMapping("/member/rank/setting")
    public String rankSetting(@Valid MemberRankSettingVo memberRankSettingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberRankSettingVo apiData = new P_MemberRankSettingVo(memberRankSettingVo, request);
        String result = memberService.callRankSetting(apiData);
        return result;
    }


    /**
     * ํ์ ์๋ฆผ๋ฐ๊ธฐ ๋ฑ๋ก/ํด์ 
     */
    @PostMapping("/member/recv")
    public String recvEdit(@Valid MemberReceiveVo memberReceiveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberReceiveVo apiData = new P_MemberReceiveVo(memberReceiveVo, request);
        String result = memberService.callRecvEdit(apiData);
        return result;
    }


    /**
     * ํ์ ์๋ฆผ๋ฐ๊ธฐ ์ญ์ 
     */
    @PostMapping("/member/recv/delete")
    public String recvDelete(@Valid MemberReceiveDeleteVo memberReceiveDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MemberReceiveDeleteVo apiData = new P_MemberReceiveDeleteVo(memberReceiveDeleteVo, request);
        String result = memberService.callRecvDelete(apiData);
        return result;
    }


    /**
     * ์๋ฆผ๋ฐ๊ธฐ ํ์ ์กฐํ
     */
    @GetMapping("/member/recv")
    public String recvList(HttpServletRequest request) throws GlobalException{
        P_MemberReceiveListVo apiData = new P_MemberReceiveListVo(request);
        String result = memberService.callRecvList(apiData);
        return result;
    }


    /**
     * ์ถ์ฒ DJ ๋ชฉ๋ก ์กฐํ
     */
    @GetMapping("/dj/recommend")
    public String djRecommendList(@Valid DjRecommendListVo djRecommendListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_DjRecommendListVo apiData = new P_DjRecommendListVo(djRecommendListVo, request);
        String result = memberService.callDjRecommendList(apiData);
        return result;
    }


    /**
     * ํ์ ์ด๋ฏธ์ง ์ ๊ณ 
     */
    @PostMapping("/report/image")
    public String reportImage(@Valid ReportImageVo reportImageVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ReportImageVo apiData = new P_ReportImageVo(reportImageVo, request);
        String result = memberService.callReportImage(apiData);
        return result;
    }

    /**
     * ๋ฒ ์คํธ DJ๋ค์ ํฌ๋ญํน (1,2,3์) ๋ฆฌ์คํธ
     */
    @GetMapping("/dj/best/fan/rank/list")
    public String djBestFanRankList(@Valid SpecialHistoryVo specialHistoryVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        return memberService.callDjBestFanRankList(specialHistoryVo, request);
    }
}

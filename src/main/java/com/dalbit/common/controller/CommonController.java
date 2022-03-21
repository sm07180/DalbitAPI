package com.dalbit.common.controller;

import com.dalbit.common.annotation.NoLogging;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.*;
import com.dalbit.common.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.util.*;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;

import static com.dalbit.common.code.Status.조회;

@Slf4j
@RestController
@Scope("prototype")
public class CommonController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    @Autowired
    MemberService memberService;

    @Autowired
    MypageService mypageService;

    @Autowired
    IPUtil ipUtil;

    @GetMapping("/splash")
    public String getSplash(HttpServletRequest request){
        //return gsonUtil.toJson(new SplashVo(Status.조회, commonService.getCodeCache("splash"), commonService.getJwtTokenInfo(request).get("tokenVo")));
        HashMap resultMap = new HashMap();

        DeviceVo deviceVo = new DeviceVo(request);
        if(!DalbitUtil.isEmpty(deviceVo) && (deviceVo.getOs() == 1 && DalbitUtil.versionCompare("1.5.8", deviceVo.getAppVersion()))
                || (deviceVo.getOs() == 2 && DalbitUtil.versionCompare("1.5.9", deviceVo.getAppVersion()))){
            resultMap = commonService.getCodeCache("splash", request);
        }else{
            resultMap = commonService.getCodeCache2("splash", request);
        }

        return gsonUtil.toJson(new JsonOutputVo(조회, commonService.getItemVersion(resultMap, request)));
    }

    @GetMapping("/items")
    public String getItems(HttpServletRequest request){
        HashMap resultMap = new HashMap();
        return gsonUtil.toJson(new JsonOutputVo(조회, commonService.getItemVersion(resultMap, request, "items")));
    }

    @PostMapping("/splash")
    public String updateSplash(){
        return gsonUtil.toJson(new JsonOutputVo(조회, commonService.updateCodeCache("splash")));
    }

    @NoLogging
    @GetMapping("/check/service")
    public String checkService(){
        return "pong";
    }

    /**
     * 위치정보가져오기
     */
    @GetMapping("/location")
    public LocationVo GetLocateByIp(HttpServletRequest request) {
        return DalbitUtil.getLocation(request);
    }

    @NoLogging
    @GetMapping("/ctrl/check/service")
    public String checkService(HttpServletRequest request){
        return commonService.checkHealthy(request);
    }

    /**
     * 휴대폰 인증요청
     */
    @PostMapping("/sms")
    public String requestSms(@Valid SmsVo smsVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        if(!DalbitUtil.isSmsPhoneNoChk(smsVo.getPhoneNo())){
            throw new GlobalException(Status.인증번호요청_유효하지않은번호, Thread.currentThread().getStackTrace()[1].getMethodName());
        }
        //휴대폰번호 '-' 치환
        smsVo.setPhoneNo(smsVo.getPhoneNo().replaceAll("-",""));
        DeviceVo deviceVo = new DeviceVo(request);
        LocationVo locationVo = DalbitUtil.getLocation(deviceVo.getIp());

        P_LoginVo pLoginVo = new P_LoginVo(
                DalbitUtil.getProperty("sms.send.default.memSlct")
                , smsVo.getPhoneNo()
                , DalbitUtil.getProperty("sms.send.default.password")
                , deviceVo.getOs()
                , deviceVo.getDeviceUuid()
                , deviceVo.getDeviceToken()
                , deviceVo.getAppVersion()
                , deviceVo.getAdId()
                , locationVo.getRegionName()
                , deviceVo.getIp()
                , DalbitUtil.getUserAgent(request)
                , deviceVo.getAppBuild()
        );

        ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
        log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));
        log.debug("결과코드 : {}", LoginProcedureVo.getRet());

        boolean isJoin = LoginProcedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode());
        //boolean isPassword = LoginProcedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode());  추후 이전 비밀번호 체크시...
        int authType = smsVo.getAuthType();
        int code = DalbitUtil.getSmscode();
        boolean isRequestSms = false;
        String result="";

        log.debug("인증타입: {}", authType);
        log.debug("휴대폰 번호: {}", smsVo.getPhoneNo());
        log.debug("휴대폰 인증코드: {}", code);
        smsVo.setCode(code);
        smsVo.setSendPhoneNo(DalbitUtil.getProperty("sms.send.phone.no"));
        smsVo.setUmId(DalbitUtil.getProperty("sms.umid"));
        smsVo.setText(DalbitUtil.getProperty("sms.text"));
        smsVo.setMemNo(MemberVo.getMyMemNo(request));

        if(isJoin && DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.join")) == authType){
            // 회원가입 필요
            isRequestSms = true;
        }else if(!isJoin && DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.password")) == authType /*&& isPassword*/ ){
            //회원이면서 비밀번호 변경
            isRequestSms = true;
        }else{
            // 이미 가입된 회원이 회원가입 할 경우
            if (DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.join")) == authType){
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입));
            // 비회원이 패스워드 변경을 요청한 경우
            } else if(DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.password")) == authType) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.비밀번호변경_인증요청_회원아님));
            // 예외
            } else {
                try {
                    response.addCookie(CookieUtil.deleteCookie("smsCookie", "", "/", 0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청실패));
            }
        }

        // 위의 예외처리에 걸리지 않은 경우 인증요청
        if(isRequestSms){
            commonService.requestSms(smsVo);
            long reqTime = System.currentTimeMillis();
            HashMap smsCookie = new HashMap();
            smsCookie.put("CMID", smsVo.getCMID());
            smsCookie.put("code", code);
            smsCookie.put("reqTime", reqTime);
            smsCookie.put("authYn", "N");
            smsCookie.put("phoneNo", smsVo.getPhoneNo());

            Cookie cookie = new Cookie("smsCookie", URLEncoder.encode(gsonUtil.toJson(smsCookie)));
            cookie.setPath("/");
            cookie.setMaxAge(300);
            response.addCookie(cookie);

            SmsOutVo smsOutVo = new SmsOutVo();
            smsOutVo.setCMID(smsVo.getCMID());

            result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청, smsOutVo));
        }
        return result;

    }

    /**
     * 휴대폰 인증확인
     */
    @PostMapping("/sms/auth")
    public String isSms(@Valid SmsCheckVo smsCheckVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        long checkTime = System.currentTimeMillis();
        String result;
        HashMap cookieResMap = null;

        CookieUtil cookieUtil = new CookieUtil(request);
        Cookie smsCookie = cookieUtil.getCookie("smsCookie");
        if (DalbitUtil.isEmpty(smsCookie)) {
            return gsonUtil.toJson(new JsonOutputVo(Status.인증실패));
        }else{
            cookieResMap = new Gson().fromJson(URLDecoder.decode(smsCookie.getValue()), HashMap.class);
        }


        int id = DalbitUtil.getIntMap(cookieResMap, "CMID");
        int code =DalbitUtil.getIntMap(cookieResMap, "code");
        long reqTime = DalbitUtil.getLongMap(cookieResMap, "reqTime");

        log.debug("휴대폰 인증요청 CMID 일치여부: {}", id==smsCheckVo.getCMID());
        log.debug("(확인시간 - 요청시간) 시간차이(s): {}",  (checkTime-reqTime)/1000 + "초");

        if(cookieResMap.get("authYn").equals("N")){
            if(id == smsCheckVo.getCMID()){
                if(code == smsCheckVo.getCode()){
                    if(DalbitUtil.isSeconds(reqTime, checkTime)){
                        result = gsonUtil.toJson(new JsonOutputVo(Status.인증확인));
                    }else {
                        result = gsonUtil.toJson(new JsonOutputVo(Status.인증시간초과));
                    }
                } else {
                    result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호불일치));
                }
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.인증CMID불일치));
            }
        } else {
            try {
                response.addCookie(CookieUtil.deleteCookie("smsCookie", "", "/", 0));
            } catch (IOException e) {
                e.printStackTrace();
            }
            result = gsonUtil.toJson(new JsonOutputVo(Status.인증실패));
        }

        log.debug("인증상태 확인 authYn: {}", new Gson().toJson(smsCookie));

        return result;
    }


    /**
     * 본인인증 요청
     */
    @PostMapping("self/auth/req")
    public String requestSelfAuth(SelfAuthVo selfAuthVo, HttpServletRequest request){
        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        customHeader = java.net.URLDecoder.decode(customHeader);
        HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);
        int os = DalbitUtil.getIntMap(headers,"os");
        String isHybrid = DalbitUtil.getStringMap(headers,"isHybrid");
        isHybrid = DalbitUtil.isEmpty(isHybrid) ? "N" : isHybrid;

        SelfAuthOutVo selfAuthOutVo = new SelfAuthOutVo();
        if(isHybrid.equals("Y")){
            selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.app.code"));    //앱 전용 URL코드
            selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.app.url"));    //앱 전용 결과수신URL
        }else{
            String startUrl = request.getHeader("referer");

            if(startUrl.startsWith("https://m.") || startUrl.startsWith("https://devm.") || startUrl.startsWith("https://devm2.")){
                selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.app.code"));    //앱 전용 URL코드
                selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.app.url"));    //앱 전용 결과수신URL
            } else {
                selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.code"));        //URL코드
                selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.url"));        //결과수신URL
            };

        }
        selfAuthVo.setCpId(DalbitUtil.getProperty("self.auth.cp.id"));                  //회원사ID
        selfAuthVo.setDate(DalbitUtil.getReqDay());                                     //요청일시
        selfAuthVo.setCertNum(DalbitUtil.getReqNum(selfAuthVo.getDate()));              //요청번호
        if(StringUtils.equals(selfAuthVo.getPageCode(), "7")) {
            selfAuthVo.setPlusInfo(selfAuthVo.getMemNo()+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType()+"_"+selfAuthVo.getAgreeTerm()
                + "_" + selfAuthVo.getPushLink()
            );
        }else {
            selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType()+"_"+selfAuthVo.getAgreeTerm()
                + "_" + selfAuthVo.getPushLink()
            );
        }

        /*if(selfAuthVo.getAuthType().equals("0")){
            selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType()
            );
        } else {
            selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType()+"_"+selfAuthVo.getAgreeTerm());
        }*/


        selfAuthOutVo.setTr_add(DalbitUtil.getProperty("self.auth.tr.add"));            //IFrame사용여부
        selfAuthOutVo.setTr_cert(DalbitUtil.getEncAuthInfo(selfAuthVo));                //요정정보(암호화)

        log.info("URL CODE: {}", selfAuthVo.getUrlCode());
        log.info("[API] #### selfAuthVo: {}", selfAuthVo);
        return gsonUtil.toJson(new JsonOutputVo(Status.본인인증요청, selfAuthOutVo));
    }

    private int birthToAmericanAge(int birthYear, int birthMonth, int birthDay) {
        Calendar cal = Calendar.getInstance();
        int nowYear = cal.get(Calendar.YEAR);
        int nowMonth = cal.get(Calendar.MONTH) + 1;
        int nowDay = cal.get(Calendar.DAY_OF_MONTH);
        int monthAndDay = Integer.parseInt(nowMonth + "" + nowDay);

        int birthMonthAndDay = Integer.parseInt(birthMonth + "" + birthDay);
        int yearDiff = nowYear - birthYear;
        int monthAndDayDiff = monthAndDay - birthMonthAndDay;
        int manAge = yearDiff;

        if(monthAndDayDiff < 0) {
            manAge--;
        }

        return manAge;
    }

    /**
     * 본인인증 확인
     */
    @PostMapping("self/auth/res")
    public String responseSelfAuthChk(@Valid SelfAuthChkVo selfAuthChkVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException, ParseException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("[API] #### 본인인증 확인 selfAuthChkVo: {}", selfAuthChkVo);
        SelfAuthSaveVo selfAuthSaveVo = DalbitUtil.getDecAuthInfo(selfAuthChkVo, request);
        log.info("[API] #### selfAuthSaveVo: {}", selfAuthSaveVo);
        String result;
        if (selfAuthSaveVo.getMsg().equals("정상")) {
            P_SelfAuthVo apiData = new P_SelfAuthVo();
            String[] plusInfo = selfAuthSaveVo.getPlusInfo().split("_");

            apiData.setMem_no(plusInfo[0]); //요청시 보낸 회원번호 (추가정보)
            apiData.setName(selfAuthSaveVo.getName());
            apiData.setPhoneNum(selfAuthSaveVo.getPhoneNo());
            apiData.setMemSex(selfAuthSaveVo.getGender());
            apiData.setBirthYear(selfAuthSaveVo.getBirthDay().substring(0, 4));
            apiData.setBirthMonth(selfAuthSaveVo.getBirthDay().substring(4, 6));
            apiData.setBirthDay(selfAuthSaveVo.getBirthDay().substring(6, 8));
            apiData.setCommCompany(selfAuthSaveVo.getPhoneCorp());
            apiData.setForeignYN(selfAuthSaveVo.getNation());
            apiData.setCertCode(selfAuthSaveVo.getCI());
            apiData.setOs(plusInfo[1]);
            apiData.setIsHybrid(plusInfo[2]);
            apiData.setPageCode(plusInfo[3]);
            apiData.setAuthType(plusInfo[4]);
            apiData.setAgreeTerm(plusInfo[5]);

            int manAge = birthToAmericanAge(
                Integer.parseInt(apiData.getBirthYear()),
                Integer.parseInt(apiData.getBirthMonth()),
                Integer.parseInt(apiData.getBirthDay())
            );

            if(selfAuthSaveVo.getPlusInfo().split("_")[4].equals("0")){

                // 만 14세 미만 이용불가
                if(manAge < 14){
                    return gsonUtil.toJson(new JsonOutputVo(Status.본인인증14세미만, apiData));
                }

                log.info("##### 본인인증 DB저장 #####");
                //회원본인인증 DB 저장
                apiData.setParents_agreeYn("n");
                result = commonService.callMemberCertification(apiData);
            } else {

                // 만 19세 미만 이용불가
                if(manAge < 19){
                    return gsonUtil.toJson(new JsonOutputVo(Status.보호자인증20세미만, apiData));
                }

                log.info("##### 보호자인증 DB업데이트 #####");
                apiData.setParents_agreeDt(DalbitUtil.getDate("yyyy-MM-dd HH:mm:ss"));
                apiData.setParents_agreeTerm(selfAuthSaveVo.getPlusInfo().split("_")[5]);
                //회원본인인증 DB 보호자정보 업데이트
                result = commonService.updateMemberCertification(apiData);
            }

        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.본인인증실패, selfAuthSaveVo.getMsg()));
        }
        return  result;
    }


    /**
     * 본인인증 여부 체크
     */
    @GetMapping("/self/auth/check")
    public String getCertificationChk(HttpServletRequest request){

        P_SelfAuthChkVo apiData = new P_SelfAuthChkVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));

        String result = commonService.getCertificationChk(apiData);

        return result;
    }


    /**
     * 에러 로그 저장
     */
    @PostMapping("/error/log")
    public String saveErrorLog(@Valid ErrorLogVo errorLogVo, BindingResult bindingResult, HttpServletRequest request, HttpServletResponse response) throws GlobalException{
        DalbitUtil.setHeader(request, response);
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ErrorLogVo apiData = new P_ErrorLogVo(errorLogVo, request);

        String result = commonService.saveErrorLog(apiData, request);

        return result;
    }

    /**
     * 앱 보안 - 에러 로그 저장
     */
    @PostMapping("/security/log")
    public String saveAppErrorLog(@Valid AppErrorLogVO appErrorLogVO, HttpServletRequest request, HttpServletResponse response) {
        DalbitUtil.setHeader(request, response);
        String result = gsonUtil.toJson(new JsonOutputVo(Status.앱보안_로그저장_성공));
        try {
            if (StringUtils.equals(appErrorLogVO.getUid(), null)) {
                appErrorLogVO.setUid("null");
            }
            if (StringUtils.equals(appErrorLogVO.getReason(), null)) {
                result = gsonUtil.toJson(new JsonOutputVo(Status.앱보안_로그저장_실패));
                appErrorLogVO.setReason("null");
            }
            String errorMsg = "UID : \"" + appErrorLogVO.getUid() + "\" / Reason : \"" + appErrorLogVO.getReason() + "\"";
            log.error( "app security log Success => {}", errorMsg);

            return result;
        } catch (Exception e) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.앱보안_로그저장_실패));
            String errorMsg = "UID : \"" + appErrorLogVO.getUid() + "\"/ Reason : \"" + appErrorLogVO.getReason() + "\"";
            log.error("app security log error Fail => {} {}", errorMsg, e);

            return result;
        }
    }


    /**
     * PUSH 발송 추가
     */
    @PostMapping("/push")
    public String pushAdd(@Valid PushVo pushVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_PushVo apiData = new P_PushVo(pushVo, request);

        String result = commonService.callPushAdd(apiData);

        return result;
    }


    /**
     * PUSH Click
     */
    @PostMapping("/push/click")
    public String pushClick(@Valid PushClickVo pushClickVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_PushClickVo apiData = new P_PushClickVo(pushClickVo, request);

        String result = commonService.callPushClickUpdate(apiData);

        return result;
    }

    @PostMapping("/social/google/callback")
    public String pushAdd(HttpServletRequest request){
        return commonService.connectGoogleNative(request);
    }

    @PostMapping("/social/facebook/callback")
    public String pushAddFacebook(HttpServletRequest request){
        return commonService.connectFacebookNative(request);
    }

    @RequestMapping(value = "/social/{type:kakao|naver|facebook|google|apple}/callback", method = RequestMethod.GET)
    public String socialCommon(@PathVariable String type, HttpServletRequest request) {
        String result = "";
        try {
            switch (type) {
                case "google":
                    result = commonService.connectGoogleNative(request);
                    break;
                case "facebook":
                    result = commonService.connectFacebookNative(request);
                    break;
                case "naver":
                    result = commonService.connectNaverNative(request);
                    break;
                case "kakao":
                    result = commonService.connectKakaoNative(request);
                    break;
                default:
            }
        } catch (Exception e) {
            log.error("CommonController / socialCommon / type: " + type + e);
        }
        return result;
    }

    /**
     * 방송주제 리스트 가져오기(사용중인 것만 가져옴)
     */
    @GetMapping("/room/type")
    public String selectRoomTypeCodeList(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(조회, commonService.selectRoomTypeCodeList(new CodeVo(Code.방송주제.getCode()))));
    }

    /**
     * 클립 리스트 가져오기(사용중인 것만 가져옴)
     */
    @GetMapping("/clip/type")
    public String selectClipTypeCodeList(HttpServletRequest request){
        return gsonUtil.toJson(new JsonOutputVo(조회, commonService.selectClipTypeCodeList(new CodeVo(Code.클립주제.getCode()))));
    }

    /**
     * IOS 심사여부 조회
     */
    @GetMapping("/ios/judge")
    public String selectIosJudge(HttpServletRequest request){

        DeviceVo deviceVo = new DeviceVo(request);
        int os = deviceVo.getOs();

        HashMap resultMap = new HashMap();
        String iosJudgeYn = "N";

        if(os == 2){
            CodeVo codeVo = new CodeVo();
            codeVo.setCd(Code.IOS심사중여부.getCode());
            codeVo.setCdNm(Code.IOS심사중여부.getDesc());
            CodeVo getCode = commonService.selectCodeDefine(codeVo);

            if(!DalbitUtil.isEmpty(getCode)){
                iosJudgeYn = getCode.getValue();
            }
        }

        resultMap.put("iosJudgeYn", iosJudgeYn);
        return gsonUtil.toJson(new JsonOutputVo(조회, resultMap));

    }

    /**
     * 장기 미접속 & 탈퇴 예정 일자 조회
     */
    @GetMapping("/long/term")
    public String getLongTermDate(HttpServletRequest request){
        P_LongTermVo apiData = new P_LongTermVo(request);
        String result = commonService.getLongTermDate(apiData);
        return result;
    }

    @PostMapping("/pay/result/callback")
    public String payRestAPITest(HttpServletRequest request){
        String clientIP = ipUtil.getClientIP(request);
        if (ipUtil.validationInnerIP(clientIP) || ipUtil.isInnerIP(clientIP)) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("memNo", request.getParameter("memNo"));
            map.put("orderId", request.getParameter("orderId"));
            map.put("clientIP", clientIP);
            map.put("validationInnerIP", ipUtil.validationInnerIP(clientIP));
            map.put("isInnerIP", ipUtil.isInnerIP(clientIP));
            log.error("test map =>{}", map);
            return gsonUtil.toJson(new JsonOutputVo(Status.본인인증확인, map));
        }else {
            return gsonUtil.toJson(new JsonOutputVo(Status.차단_이용제한));
        }
    }

    @PostMapping("/sleep/member/update")
    public String sleepMemChkUpd(@RequestParam(value = "memNo") String memNo, @RequestParam(value = "memPhone") String memPhone) {
        return commonService.sleepMemChkUpd(memNo, memPhone);
    }
}

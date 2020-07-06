package com.dalbit.common.controller;

import com.dalbit.common.annotation.NoLogging;
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
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.RedisUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.HashMap;

@Slf4j
@RestController
public class CommonController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

    @Autowired
    MemberService memberService;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    MypageService mypageService;

    @GetMapping("/splash")
    public String getSplash(HttpServletRequest request){
        //return gsonUtil.toJson(new SplashVo(Status.조회, commonService.getCodeCache("splash"), commonService.getJwtTokenInfo(request).get("tokenVo")));
        HashMap resultMap = commonService.getCodeCache("splash", request);
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.getItemVersion(resultMap, request)));
    }

    @GetMapping("/items")
    public String getItems(HttpServletRequest request){
        HashMap resultMap = new HashMap();
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.getItemVersion(resultMap, request, "items")));
    }

    @PostMapping("/splash")
    public String updateSplash(){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.updateCodeCache("splash")));
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
    public String requestSms(@Valid SmsVo smsVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

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
                result = gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요));
            // 예외
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청실패));
            }
            request.getSession().invalidate();
        }

        // 위의 예외처리에 걸리지 않은 경우 인증요청
        if(isRequestSms){
            commonService.requestSms(smsVo);

            HttpSession session = request.getSession();
            long reqTime = System.currentTimeMillis();
            session.setAttribute("CMID", smsVo.getCMID());
            session.setAttribute("code", code);
            session.setAttribute("reqTime", reqTime);
            session.setAttribute("authYn", "N");
            session.setAttribute("phoneNo", smsVo.getPhoneNo());

            SmsOutVo smsOutVo = new SmsOutVo();
            smsOutVo.setCMID(smsVo.getCMID());

            result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청, smsOutVo));
        }

        HttpSession session = request.getSession();
        log.debug("SESSION ID : {}", session.getId());
        log.debug("SESSION CMID : {}", session.getAttribute("CMID"));
        log.debug("SESSION code : {}", session.getAttribute("code"));
        log.debug("SESSION reqTime : {}", session.getAttribute("reqTime"));

        return result;

    }

    /**
     * 휴대폰 인증확인
     */
    @PostMapping("/sms/auth")
    public String isSms(@Valid SmsCheckVo smsCheckVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        long checkTime = System.currentTimeMillis();

        HttpSession session = request.getSession();

        String result;
        log.debug("SESSION ID : {}", session.getId());
        log.debug("SESSION CMID : {}", session.getAttribute("CMID"));
        log.debug("SESSION code : {}", session.getAttribute("code"));
        log.debug("SESSION reqTime : {}", session.getAttribute("reqTime"));
        int id = (int) session.getAttribute("CMID");
        int code = (int) session.getAttribute("code");
        long reqTime = (long) session.getAttribute("reqTime");

        log.debug("휴대폰 인증요청 CMID 일치여부: {}", id==smsCheckVo.getCMID());
        log.debug("(확인시간 - 요청시간) 시간차이(s): {}",  (checkTime-reqTime)/1000 + "초");

        if(session.getAttribute("authYn").equals("N")){
            if(id == smsCheckVo.getCMID()){
                if(code == smsCheckVo.getCode()){
                    if(DalbitUtil.isSeconds(reqTime, checkTime)){
                        session.setAttribute("authYn", "Y");
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
            result = gsonUtil.toJson(new JsonOutputVo(Status.인증실패));
            request.getSession().invalidate();
        }

        log.debug("인증상태 확인 authYn: {}", session.getAttribute("authYn"));

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
            if(os != 3 ){
                selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.app.code"));    //앱 전용 URL코드
                selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.app.url"));    //앱 전용 결과수신URL
            } else {
                selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.code"));        //URL코드
                selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.url"));        //결과수신URL
            }
        }
        selfAuthVo.setCpId(DalbitUtil.getProperty("self.auth.cp.id"));                  //회원사ID
        selfAuthVo.setDate(DalbitUtil.getReqDay());                                     //요청일시
        selfAuthVo.setCertNum(DalbitUtil.getReqNum(selfAuthVo.getDate()));              //요청번호
        if(selfAuthVo.getAuthType().equals("0")){
            selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType());
        } else {
            selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid+"_"+selfAuthVo.getPageCode()+"_"+selfAuthVo.getAuthType()+"_"+selfAuthVo.getAgreeTerm());
        }


        selfAuthOutVo.setTr_add(DalbitUtil.getProperty("self.auth.tr.add"));            //IFrame사용여부
        selfAuthOutVo.setTr_cert(DalbitUtil.getEncAuthInfo(selfAuthVo));                //요정정보(암호화)

        log.info("URL CODE: {}", selfAuthVo.getUrlCode());

        return gsonUtil.toJson(new JsonOutputVo(Status.본인인증요청, selfAuthOutVo));
    }

    /**
     * 본인인증 확인
     */
    @PostMapping("self/auth/res")
    public String responseSelfAuthChk(@Valid SelfAuthChkVo selfAuthChkVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException, ParseException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        log.info("[API] #### 본인인증 확인 selfAuthChkVo: {}", selfAuthChkVo);
        SelfAuthSaveVo selfAuthSaveVo = DalbitUtil.getDecAuthInfo(selfAuthChkVo, request);

        String result;
        if (selfAuthSaveVo.getMsg().equals("정상")) {
            P_SelfAuthVo apiData = new P_SelfAuthVo();

            apiData.setMem_no(selfAuthSaveVo.getPlusInfo().split("_")[0]); //요청시 보낸 회원번호 (추가정보)
            apiData.setName(selfAuthSaveVo.getName());
            apiData.setPhoneNum(selfAuthSaveVo.getPhoneNo());
            apiData.setMemSex(selfAuthSaveVo.getGender());
            apiData.setBirthYear(selfAuthSaveVo.getBirthDay().substring(0, 4));
            apiData.setBirthMonth(selfAuthSaveVo.getBirthDay().substring(4, 6));
            apiData.setBirthDay(selfAuthSaveVo.getBirthDay().substring(6, 8));
            apiData.setCommCompany(selfAuthSaveVo.getPhoneCorp());
            apiData.setForeignYN(selfAuthSaveVo.getNation());
            apiData.setCertCode(selfAuthSaveVo.getCI());
            apiData.setOs(selfAuthSaveVo.getPlusInfo().split("_")[1]);
            apiData.setIsHybrid(selfAuthSaveVo.getPlusInfo().split("_")[2]);
            apiData.setPageCode(selfAuthSaveVo.getPlusInfo().split("_")[3]);
            apiData.setAuthType(selfAuthSaveVo.getPlusInfo().split("_")[4]);

            if(DalbitUtil.getAge(Integer.parseInt(selfAuthSaveVo.getBirthDay().substring(0, 4)), Integer.parseInt(selfAuthSaveVo.getBirthDay().substring(4, 6)), Integer.parseInt(selfAuthSaveVo.getBirthDay().substring(6, 8))) < 19){
                apiData.setAdultYn("n");
            } else {
                apiData.setAdultYn("y");
            }

            if(selfAuthSaveVo.getPlusInfo().split("_")[4].equals("0")){
                log.info("##### 본인인증 DB저장 #####");
                //회원본인인증 DB 저장
                apiData.setParents_agreeYn("n");
                result = commonService.callMemberCertification(apiData);
            } else {
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
        //return gsonUtil.toJson(new JsonOutputVo(Status.본인인증여부_확인));
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

    @PostMapping("/inforex/broadCheck")
    public String broadCheck(HttpServletRequest request){
        return commonService.selectNowBroadcast(request);
    }
}

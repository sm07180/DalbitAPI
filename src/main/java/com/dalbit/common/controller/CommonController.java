package com.dalbit.common.controller;

import com.dalbit.common.annotation.NoLogging;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.common.vo.procedure.P_PushVo;
import com.dalbit.common.vo.procedure.P_SelfAuthChkVo;
import com.dalbit.common.vo.procedure.P_SelfAuthVo;
import com.dalbit.common.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_LoginVo;
import com.dalbit.member.vo.procedure.P_MemberInfoVo;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.getCodeCache("splash")));
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

    @GetMapping("/store")
    public String tmpStore(HttpServletRequest request)
    {
        int dalCnt = 0;
        if(DalbitUtil.isLogin(request)){
            P_MemberInfoVo apiData = new P_MemberInfoVo();
            apiData.setMem_no(MemberVo.getMyMemNo(request));
            String result = mypageService.callMemberInfo(apiData);
            HashMap infoView = new Gson().fromJson(result, HashMap.class);
            if(!DalbitUtil.isEmpty(infoView) && !DalbitUtil.isEmpty(infoView.get("result")) && "success".equals(infoView.get("result").toString()) && !DalbitUtil.isEmpty(infoView.get("data"))){
                HashMap infoMap = new Gson().fromJson(new Gson().toJson(infoView.get("data")), HashMap.class);
                if(!DalbitUtil.isEmpty(infoMap) && !DalbitUtil.isEmpty(infoMap.get("dalCnt"))){
                    dalCnt = DalbitUtil.getIntMap(infoMap, "dalCnt");
                }
            }
        }

        List<HashMap> list = new ArrayList<>();
        HashMap store1 = new HashMap();
        store1.put("storeNo", "3001");
        store1.put("storeNm", "달 10");
        store1.put("price", 3000);
        store1.put("priceNm", "");
        store1.put("img", "https://image.dalbitlive.com/store/store_1.png");
        store1.put("appleStoreId", "com.dalbitcast.inapp.dal10");
        HashMap store2= new HashMap();
        store2.put("storeNo", "3002");
        store2.put("storeNm", "달 30");
        store2.put("price", 10000);
        store2.put("priceNm", "");
        store2.put("img", "https://image.dalbitlive.com/store/store_2.png");
        store2.put("appleStoreId", "com.dalbitcast.inapp.dal30");
        HashMap store3 = new HashMap();
        store3.put("storeNo", "3003");
        store3.put("storeNm", "달 100");
        store3.put("price", 30000);
        store3.put("priceNm", "");
        store3.put("img", "https://image.dalbitlive.com/store/store_3.png");
        store3.put("appleStoreId", "com.dalbitcast.inapp.dal100");
        HashMap store4 = new HashMap();
        store4.put("storeNo", "3004");
        store4.put("storeNm", "달 500");
        store4.put("price", 50000);
        store4.put("priceNm", "");
        store4.put("img", "https://image.dalbitlive.com/store/store_4.png");
        store4.put("appleStoreId", "com.dalbitcast.inapp.dal500");
        HashMap store5 = new HashMap();
        store5.put("storeNo", "3005");
        store5.put("storeNm", "달 1000");
        store5.put("price", 100000);
        store5.put("priceNm", "");
        store5.put("img", "https://image.dalbitlive.com/store/store_5.png");
        store5.put("appleStoreId", "com.dalbitcast.inapp.dal1000");
        HashMap store6 = new HashMap();
        store6.put("storeNo", "3006");
        store6.put("storeNm", "달 3000");
        store6.put("price", 300000);
        store6.put("priceNm", "");
        store6.put("img", "https://image.dalbitlive.com/store/store_6.png");
        store6.put("appleStoreId", "com.dalbitcast.inapp.dal3000");
        list.add(store1);
        list.add(store2);
        list.add(store3);
        list.add(store4);
        list.add(store5);
        list.add(store6);

        HashMap returnMap = new HashMap();
        returnMap.put("dalCnt", dalCnt);
        returnMap.put("list", list);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, returnMap));
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

        selfAuthVo.setCpId(DalbitUtil.getProperty("self.auth.cp.id"));          //회원사ID
        selfAuthVo.setUrlCode(DalbitUtil.getProperty("self.auth.url.code"));    //URL코드
        selfAuthVo.setDate(DalbitUtil.getReqDay());                             //요청일시
        selfAuthVo.setCertNum(DalbitUtil.getReqNum(selfAuthVo.getDate()));      //요청번호
        selfAuthVo.setPlusInfo(MemberVo.getMyMemNo(request)+"_"+os+"_"+isHybrid);

        SelfAuthOutVo selfAuthOutVo = new SelfAuthOutVo();
        selfAuthOutVo.setTr_cert(DalbitUtil.getEncAuthInfo(selfAuthVo));        //요정정보(암호화)
        selfAuthOutVo.setTr_url(DalbitUtil.getProperty("self.auth.tr.url"));    //결과수신URL
        selfAuthOutVo.setTr_add(DalbitUtil.getProperty("self.auth.tr.add"));    //IFrame사용여부

        log.info("URL CODE: {}", selfAuthVo.getUrlCode());

        return gsonUtil.toJson(new JsonOutputVo(Status.본인인증요청, selfAuthOutVo));
    }

    /**
     * 본인인증 확인
     */
    @PostMapping("self/auth/res")
    public String responseSelfAuthChk(@Valid SelfAuthChkVo selfAuthChkVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException, ParseException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
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
            apiData.setOs((selfAuthSaveVo.getPlusInfo().split("_")[1]));
            apiData.setIsHybrid((selfAuthSaveVo.getPlusInfo().split("_")[2]));

            //회원본인인증 DB 저장
            result = commonService.callMemberCertification(apiData);
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

        String result = commonService.saveErrorLog(apiData);

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
}

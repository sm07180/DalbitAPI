package com.dalbit.common.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MemberService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
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

    @GetMapping("/splash")
    public String getSplash(HttpServletRequest request){
        //return gsonUtil.toJson(new SplashVo(Status.조회, commonService.getCodeCache("splash"), commonService.getJwtTokenInfo(request).get("tokenVo")));
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.getCodeCache("splash")));
    }

    @PostMapping("/splash")
    public String updateSplash(){
        return gsonUtil.toJson(new JsonOutputVo(Status.조회, commonService.updateCodeCache("splash")));
    }

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

    @GetMapping("/ctrl/check/service")
    @ResponseBody
    public String checkService(HttpServletRequest request){
        return commonService.checkHealthy(request);
    }

    @GetMapping("/store")
    public String tmpStore()
    {
        List<HashMap> list = new ArrayList<>();
        HashMap store1 = new HashMap();
        store1.put("storeNo", "3001");
        store1.put("storeNm", "달 10");
        store1.put("price", 3000);
        store1.put("priceNm", "");
        store1.put("img", "https://devimage.dalbitcast.com/store/store_1.png");
        store1.put("appleStoreId", "com.dalbitcast.inapp.dal10");
        HashMap store2= new HashMap();
        store2.put("storeNo", "3002");
        store2.put("storeNm", "달 30");
        store2.put("price", 10000);
        store2.put("priceNm", "");
        store2.put("img", "https://devimage.dalbitcast.com/store/store_2.png");
        store2.put("appleStoreId", "com.dalbitcast.inapp.dal30");
        HashMap store3 = new HashMap();
        store3.put("storeNo", "3003");
        store3.put("storeNm", "달 100");
        store3.put("price", 30000);
        store3.put("priceNm", "");
        store3.put("img", "https://devimage.dalbitcast.com/store/store_3.png");
        store3.put("appleStoreId", "com.dalbitcast.inapp.dal100");
        HashMap store4 = new HashMap();
        store4.put("storeNo", "3004");
        store4.put("storeNm", "달 500");
        store4.put("price", 50000);
        store4.put("priceNm", "");
        store4.put("img", "https://devimage.dalbitcast.com/store/store_4.png");
        store4.put("appleStoreId", "com.dalbitcast.inapp.dal500");
        HashMap store5 = new HashMap();
        store5.put("storeNo", "3005");
        store5.put("storeNm", "달 1000");
        store5.put("price", 100000);
        store5.put("priceNm", "");
        store5.put("img", "https://devimage.dalbitcast.com/store/store_5.png");
        store5.put("appleStoreId", "com.dalbitcast.inapp.dal1000");
        HashMap store6 = new HashMap();
        store6.put("storeNo", "3006");
        store6.put("storeNm", "달 3000");
        store6.put("price", 300000);
        store6.put("priceNm", "");
        store6.put("img", "https://devimage.dalbitcast.com/store/store_6.png");
        store6.put("appleStoreId", "com.dalbitcast.inapp.dal3000");
        list.add(store1);
        list.add(store2);
        list.add(store3);
        list.add(store4);
        list.add(store5);
        list.add(store6);

        return gsonUtil.toJson(new JsonOutputVo(Status.조회, list));
    }

    /**
     * 휴대폰 인증요청
     */
    @PostMapping("/sms")
    public String requestSms(@Valid SmsVo smsVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult);

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
        );

        ProcedureOutputVo LoginProcedureVo = memberService.callMemberLogin(pLoginVo);
        log.debug("로그인 결과 : {}", new Gson().toJson(LoginProcedureVo));
        log.debug("결과코드 : {}", LoginProcedureVo.getRet());

        boolean isJoin = LoginProcedureVo.getRet().equals(Status.로그인실패_회원가입필요.getMessageCode());
        boolean isPassword = LoginProcedureVo.getRet().equals(Status.로그인실패_패스워드틀림.getMessageCode());
        int authType = smsVo.getAuthType();
        String result;
        int code = DalbitUtil.getSmscode();

        log.debug("인증타입: {}", authType);
        log.debug("휴대폰 번호: {}", smsVo.getPhoneNo());
        log.debug("휴대폰 인증코드: {}", code);
        smsVo.setCode(code);
        smsVo.setSendPhoneNo(DalbitUtil.getProperty("sms.send.phone.no"));

        if(isJoin && DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.join")) == authType){
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
        }else if (isPassword && DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.password")) == authType){
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
        } else {
            if (DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.join")) == authType){
                result = gsonUtil.toJson(new JsonOutputVo(Status.회원가입실패_중복가입));
            }else if(DalbitUtil.isStringToNumber(DalbitUtil.getProperty("sms.send.authType.password")) == authType){
                result = gsonUtil.toJson(new JsonOutputVo(Status.로그인실패_회원가입필요));
            } else {
                result = gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청실패));
                request.getSession().invalidate();
            }
        }

        HttpSession session = request.getSession();
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

        DalbitUtil.throwValidaionException(bindingResult);
        long checkTime = System.currentTimeMillis();

        HttpSession session = request.getSession();

        String result;
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
}

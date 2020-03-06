package com.dalbit.common.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@RestController
public class CommonController {

    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    CommonService commonService;

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



    /**
     * 휴대폰 인증요청
     */
    @PostMapping("/sms")
    public String requestSms(@Valid SmsVo smsVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult);

        int code = DalbitUtil.getSmscode();

        log.debug("휴대폰 번호: {}", smsVo.getPhoneNo());
        log.debug("휴대폰 인증 코드: {}", code);

        smsVo.setCode(code);
        smsVo.setSendPhoneNo(DalbitUtil.getProperty("sms.send.phone.no"));
        commonService.requestSms(smsVo);

        HttpSession session = request.getSession();
        long reqTime = System.currentTimeMillis();
        session.setAttribute("CMID", smsVo.getCMID());
        session.setAttribute("code", code);
        session.setAttribute("reqTime", reqTime);
        session.setAttribute("authYn", "N");

        SmsOutVo smsOutVo = new SmsOutVo();
        smsOutVo.setCMID(smsVo.getCMID());

        return gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청, smsOutVo));
    }

    /**
     * 휴대폰 인증확인
     */
    @GetMapping("/sms")
    public String isSms(@Valid SmsCheckVo smsCheckVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);
        long checkTime = System.currentTimeMillis();

        HttpSession session = request.getSession();
        int id = (int) session.getAttribute("CMID");
        int code = (int) session.getAttribute("code");
        long reqTime = (long) session.getAttribute("reqTime");

        log.debug("휴대폰 인증요청 CMID 일치여부: {}", id==smsCheckVo.getCMID());
        log.debug("(확인시간 - 요청시간) 시간차이(s): {}",  (checkTime-reqTime)/1000 + "초");

        String result;
        if(!session.getAttribute("authYn").equals("Y")){
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
                result = gsonUtil.toJson(new JsonOutputVo(Status.인증실패));
            }
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.인증확인));
        }

        log.debug("인증상태 확인 authYn: {}", session.getAttribute("authYn"));

        return result;
    }
}

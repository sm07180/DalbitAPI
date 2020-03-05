package com.dalbit.common.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.LocationVo;
import com.dalbit.common.vo.SmsOutVo;
import com.dalbit.common.vo.SmsVo;
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
        SmsOutVo smsOutVo = commonService.requestSms(smsVo);

        return gsonUtil.toJson(new JsonOutputVo(Status.인증번호요청, smsOutVo));
    }



}

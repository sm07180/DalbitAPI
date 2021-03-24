package com.dalbit.event.controller;

import com.dalbit.event.service.CamService;
import com.dalbit.event.vo.procedure.P_CamApplyVo;
import com.dalbit.event.vo.procedure.P_CamCheckVo;
import com.dalbit.event.vo.request.CamApplyVo;
import com.dalbit.event.vo.request.RoulettePhoneVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/cam")
@Scope("prototype")
public class WebCamController {

    @Autowired
    CamService camService;

    /**
     * 지원 상태 체크
     */
    @PostMapping("/status")
    public String camStatus(HttpServletRequest request){
        P_CamCheckVo apiData = new P_CamCheckVo(request);
        String result = camService.camStatus(apiData);
        return result;
    }

    /**
     * 지원 신청 체크
     */
    @PostMapping("/check")
    public String camCheck(HttpServletRequest request){
        P_CamCheckVo apiData = new P_CamCheckVo(request);
        String result = camService.callCamCheck(apiData);
        return result;
    }

    /**
     * 신청서 작성
     */
    @PostMapping("/apply")
    public String camApply(@Valid CamApplyVo camApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        String result = camService.callCamApply(request, camApplyVo);
        return result;
    }
}

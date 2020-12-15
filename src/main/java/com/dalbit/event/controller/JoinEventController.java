package com.dalbit.event.controller;

import com.dalbit.broadcast.vo.procedure.P_EventJoinDetailVo;
import com.dalbit.broadcast.vo.procedure.P_MoonCheckVo;
import com.dalbit.broadcast.vo.request.EventJoinDetailVo;
import com.dalbit.event.service.JoinService;
import com.dalbit.event.vo.procedure.P_EventJoinRewardVo;
import com.dalbit.event.vo.procedure.P_JoinCheckVo;
import com.dalbit.event.vo.procedure.P_RouletteCouponVo;
import com.dalbit.event.vo.request.EventJoinRewardVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/event/join")
public class JoinEventController {
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    JoinService joinService;

    /**
     * 가입 이벤트 팝업,배너 노출 체크
     */
    @GetMapping("/check")
    public String eventJoinCheck(HttpServletRequest request){

        P_JoinCheckVo apiData = new P_JoinCheckVo(request);
        String result = joinService.callEventJoinCheck(apiData);
        return result;
    }


    /**
     * 가입 이벤트 상세 보기
     */
    @GetMapping("/detail")
    public String eventJoinDetail(@Valid EventJoinDetailVo eventJoinDetailVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_EventJoinDetailVo apiData = new P_EventJoinDetailVo(eventJoinDetailVo, request);
        String result = joinService.callEventJoinDetail(apiData);
        return result;
    }


    /**
     * 가입 이벤트 보상받기
     */
    @PostMapping("/reward")
    public String eventJoinReward(@Valid EventJoinRewardVo eventJoinRewardVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_EventJoinRewardVo apiData = new P_EventJoinRewardVo(eventJoinRewardVo, request);
        String result = joinService.callEventJoinReward(apiData);
        return result;
    }

}

package com.dalbit.event.controller;

import com.dalbit.event.service.RouletteService;
import com.dalbit.event.vo.procedure.P_RouletteApplyListVo;
import com.dalbit.event.vo.procedure.P_RouletteCouponVo;
import com.dalbit.event.vo.procedure.P_RoulettePhoneVo;
import com.dalbit.event.vo.procedure.P_RouletteWinListVo;
import com.dalbit.event.vo.request.RouletteApplyVo;
import com.dalbit.event.vo.request.RouletteInfoVo;
import com.dalbit.event.vo.request.RoulettePhoneVo;
import com.dalbit.event.vo.request.RouletteWinVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/event/roulette")
public class RouletteController {

    @Autowired
    RouletteService rouletteService;

    /**
     * 나의 응모 기회 가져오기
     */
    @GetMapping("/coupon")
    public String couponSelect(HttpServletRequest request){

        P_RouletteCouponVo apiData = new P_RouletteCouponVo(request);
        String result = rouletteService.couponSelect(apiData);
        return result;
    }

    /**
     * 룰렛 스타트
     */
    @GetMapping("/start")
    public String rouletteStart(HttpServletRequest request){

        P_RouletteCouponVo apiData = new P_RouletteCouponVo(request);
        String result = rouletteService.rouletteStart(apiData);
        return result;
    }

    /**
     * 전화번호 입력
     */
    @PostMapping("/phone")
    public String inputPhone(@Valid RoulettePhoneVo roulettePhoneVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RoulettePhoneVo apiData = new P_RoulettePhoneVo(roulettePhoneVo, request);
        String result = rouletteService.inputPhone(apiData);
        return result;
    }

    /**
     * 나의 참여 이력 조회
     */
    @GetMapping("/apply")
    public String applyList(@Valid RouletteApplyVo rouletteApplyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RouletteApplyListVo apiData = new P_RouletteApplyListVo(rouletteApplyVo, request);
        String result = rouletteService.applyList(apiData);
        return result;
    }

    /**
     * 당첨자 조회
     */
    @GetMapping("/win")
    public String winList(@Valid RouletteWinVo rouletteWinVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RouletteWinListVo apiData = new P_RouletteWinListVo(rouletteWinVo, request);
        String result = rouletteService.winList(apiData);
        return result;
    }

    @GetMapping("/info")
    public String rouletteInfo(HttpServletRequest request, RouletteInfoVo rouletteInfoVo){
        return rouletteService.selectRouletteInfo(rouletteInfoVo);
    }
}

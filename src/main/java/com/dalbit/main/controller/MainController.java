package com.dalbit.main.controller;

import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.main.service.MainService;
import com.dalbit.main.vo.procedure.*;
import com.dalbit.main.vo.request.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
public class MainController {

    @Autowired
    MainService mainService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 메인
     */
    @GetMapping("/main")
    public String main(HttpServletRequest request){
        return mainService.getMain(request);
    }


    /**
     * 팬 랭킹
     */
    @GetMapping("/rank/fan")
    public String mainFanRanking(@Valid MainFanRankingVo mainFanRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainFanRankingVo apiData = new P_MainFanRankingVo(mainFanRankingVo, request);

        String result = mainService.callMainFanRanking(apiData);

        return result;
    }


    /**
     * DJ 랭킹
     */
    @GetMapping("/rank/dj")
    public String mainDjRanking(@Valid MainDjRankingVo mainDjRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainDjRankingVo apiData = new P_MainDjRankingVo(mainDjRankingVo, request);

        String result = mainService.callMainDjRanking(apiData);

        return result;
    }


    /**
     * Level 랭킹
     */
    @GetMapping("/rank/level")
    public String mainLevelRanking(@Valid MainLevelRankingVo mainLevelRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MainLevelRankingVo apiData = new P_MainLevelRankingVo(mainLevelRankingVo, request);

        String result = mainService.callMainLevelRanking(apiData);

        return result;
    }


    /**
     * 배너
     */
    @GetMapping("/banner")
    public String banner(HttpServletRequest request) {
        return mainService.selectBanner(request);
    }


    /**
     * 랭킹 보상 팝업
     */
    @GetMapping("/rank/reward/popup")
    public String mainRankRewardPopup(@Valid MainRankRewardVo mainRankRewardVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainRankRewardVo apiData = new P_MainRankRewardVo(mainRankRewardVo, request);

        String result = mainService.callMainRankReward(apiData);

        return result;

    }


    /**
     * 랜덤박스 열기
     */
    @PostMapping("/rank/randombox")
    public String mainRankRandomBox(@Valid MainRankRewardVo mainRankRewardVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainRankRewardVo apiData = new P_MainRankRewardVo(mainRankRewardVo, request);

        String result = mainService.callMainRankRandomBox(apiData);

        return result;

    }


    /**
     * 랭킹조회
     */
    @GetMapping("/rank/page")
    public String mainRankingPage(@Valid MainRankingPageVo mainRankingPageVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainRankingPageVo apiData = new P_MainRankingPageVo(mainRankingPageVo, request);

        String result = mainService.mainRankingPage(apiData);

        return result;
    }


    /**
     * 좋아요 랭킹
     */
    @GetMapping("/rank/good")
    public String mainGoodRanking(@Valid MainGoodRankingVo mainGoodRankingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MainGoodRankingVo apiData = new P_MainGoodRankingVo(mainGoodRankingVo, request);

        String result = mainService.mainGoodRanking(apiData);

        return result;
    }

    /**
     * 스페셜 dj 이력
     */
    @GetMapping("/specialDj/history")
    public String specialDjHistory(@Valid SpecialHistoryVo SpecialHistoryVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap result = mainService.getSpecialDjHistory(SpecialHistoryVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    /**
     * 위클리픽 & 15초 광고모델
     */
    @GetMapping(value = {"/marketing/weekly", "/marketing/second"})
    public String marketingWeeklyList(@Valid P_MarketingVo pMarketingVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int slctType = request.getRequestURI().contains("/marketing/weekly") ? 1 : 2;

        pMarketingVo.setSlctType(slctType);
        HashMap result = mainService.getMarketingList(pMarketingVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    /**
     * 위클리픽 & 15초 광고모델
     */
    @GetMapping("/marketing/detail")
    public String marketingDetail(P_MarketingVo pMarketingVo, HttpServletRequest request) throws GlobalException {

        HashMap result = mainService.marketingDetail(pMarketingVo, request);

        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }
}

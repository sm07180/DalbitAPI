package com.dalbit.member.controller;

import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.procedure.P_AwardHonorListVo;
import com.dalbit.member.vo.request.AwardHonorListVo;
import com.dalbit.member.service.AwardService;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.*;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/award")
public class AwardController {

    @Autowired
    AwardService awardService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 어워드 DJ리스트
    */
    @GetMapping("/list")
    public String awardList(@Valid AwardListVo awardListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_AwardListVo apiData = new P_AwardListVo(awardListVo, request);
        String result = awardService.callAwardList(apiData);
        return result;
    }


    /**
     * 어워드 DJ투표
     */
    @PostMapping("/vote")
    public String awardVote(@Valid AwardVoteVo awardVoteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_AwardVoteVo adpiData = new P_AwardVoteVo(awardVoteVo, request);
        String result = awardService.callAwardVote(adpiData);
        return result;
    }


    /**
     * 어워드 투표결과
     */
    @GetMapping("/result")
    public String awardVoteResult(@Valid AwardVoteResultVo awardVoteResultVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_AwardVoteResultVo apiData = new P_AwardVoteResultVo(awardVoteResultVo);
        String result = awardService.callAwardVoteResult(apiData);
        return result;
    }


    /**
     * 어워드 명예의전당
     */
    @GetMapping("/honor")
    public String awardHonorList(@Valid AwardHonorListVo awardHonorListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_AwardHonorListVo apiData = new P_AwardHonorListVo(awardHonorListVo, request);
        String result = awardService.callAwardHonorList(apiData);
        return result;
    }
}


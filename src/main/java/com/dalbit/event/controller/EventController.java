package com.dalbit.event.controller;

import com.dalbit.event.service.EventService;
import com.dalbit.event.vo.procedure.P_RankingLiveInputVo;
import com.dalbit.event.vo.procedure.P_RankingResultInputVo;
import com.dalbit.event.vo.request.RankingLiveVo;
import com.dalbit.event.vo.request.RankingResultVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * 랭킹 이벤트 실시간 순위보기
     */
    @GetMapping("/ranking/live")
    public String rankingLive(@Valid RankingLiveVo rankingLiveVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RankingLiveInputVo apiData = new P_RankingLiveInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSlct_type(rankingLiveVo.getSlctType());

        String result = eventService.callEventRankingLive(apiData);

        return result;
    }

    /**
     * 랭킹 이벤트 결과 보기
     */
    @GetMapping("/ranking/result")
    public String rankingResult(@Valid RankingResultVo rankingResultVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RankingResultInputVo apiData = new P_RankingResultInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSlct_type(rankingResultVo.getSlctType());
        apiData.setRound(rankingResultVo.getRound());

        String result = eventService.callEventRankingResult(apiData);

        return result;
    }
}

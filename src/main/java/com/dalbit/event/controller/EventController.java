package com.dalbit.event.controller;

import com.dalbit.event.service.EventService;
import com.dalbit.event.vo.procedure.*;
import com.dalbit.event.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/event")
public class EventController {

    @Autowired
    private EventService eventService;

    @GetMapping("/ranking/term")
    public String event200608Term(){
        return eventService.event200608Term();
    }

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


    /**
     * 이벤트 댓글 리스트 조회
     */
    @GetMapping("/reply")
    public String replyList(@Valid ReplyVo replyVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyListInputVo apiData = new P_ReplyListInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setEvent_idx(replyVo.getEventIdx());

        String result = eventService.callEventReplyList(apiData);

        return result;
    }


    /**
     * 이벤트 댓글 등록
     */
    @PostMapping("/reply")
    public String replyAdd(@Valid ReplyAddVo replyAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyAddInputVo apiData = new P_ReplyAddInputVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setContents(replyAddVo.getContent());
        apiData.setDepth(replyAddVo.getDepth());
        apiData.setEvent_idx(replyAddVo.getEventIdx());

        String result = eventService.callEventReplyAdd(apiData);

        return result;
    }


    /**
     * 이벤트 댓글 삭제
     */
    @DeleteMapping("/reply")
    public String replyDelete(@Valid ReplyDeleteVo replyDeleteVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ReplyDeleteInputVo apiData = new P_ReplyDeleteInputVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setReply_idx(replyDeleteVo.getReplyIdx());
        apiData.setEvent_idx(replyDeleteVo.getEventIdx());

        String result = eventService.callEventReplyDelete(apiData);

        return result;
    }

}

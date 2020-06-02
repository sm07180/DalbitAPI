package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
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
@RequestMapping("/broad")
public class ActionController {

    @Autowired
    private ActionService actionService;

    /**
     * 방송방 좋아요 추가
     */
    @PostMapping("/likes")
    public String roomGood(@Valid GoodVo goodVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomGoodVo apiData = new P_RoomGoodVo();
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setRoom_no(goodVo.getRoomNo());

        String result = actionService.callBroadCastRoomGood(apiData, request);

        return result;
    }


    /**
     * 방송방 공유링크 확인
     */
    @GetMapping("/link")
    public String roomShareLink(@Valid ShareLinkVo shareLinkVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomShareLinkVo apiData = new P_RoomShareLinkVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setLinkCode(shareLinkVo.getLink());

        String result = actionService.callBroadCastShareLink(apiData);

        return result;
    }


    /**
     * 방송방 공유 경로 조회
     */
    @GetMapping("/share")
    public String roomInfo(@Valid RoomInfo roomInfo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomInfo.getRoomNo());

        String result = actionService.callBroadCastRoomShareLink(apiData, request);

        return result;

    }

    /**
     * 방송방 선물하기
     */
    @PostMapping("/gift")
    public String roomGift(@Valid GiftVo giftVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomGiftVo apiData = new P_RoomGiftVo();
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setRoom_no(giftVo.getRoomNo());
        apiData.setGifted_mem_no(giftVo.getMemNo());
        apiData.setItem_code(giftVo.getItemNo());
        apiData.setItem_cnt(giftVo.getItemCnt());
        apiData.setSecret("1".equals(giftVo.getIsSecret()) || "TRUE".equals(giftVo.getIsSecret().toUpperCase()) ? "1" : "0");

        String result = actionService.callBroadCastRoomGift(apiData, request);

        return result;
    }


    /**
     * 부스터 사용하기
     */
    @PostMapping("/boost")
    public String roomBooster(@Valid BoosterVo boosterVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomBoosterVo apiData = new P_RoomBoosterVo();
        apiData.setMem_no(new MemberVo().getMyMemNo(request));
        apiData.setRoom_no(boosterVo.getRoomNo());
        apiData.setItem_no(boosterVo.getItemNo());
        apiData.setItem_cnt(boosterVo.getItemCnt());
        apiData.setItem_code(DalbitUtil.getProperty("item.code.boost"));
        String result = actionService.callBroadCastRoomBooster(apiData, request);

        return result;
    }


    /**
     * 방송 시간 연장
     */
    @PostMapping("/extend")
    public String roomExtendTime(@Valid ExtendTimeVo extendTimeVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_ExtendTimeVo apiData = new P_ExtendTimeVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(extendTimeVo.getRoomNo());

        String result = actionService.callroomExtendTime(apiData, request);

        return result;
    }

}

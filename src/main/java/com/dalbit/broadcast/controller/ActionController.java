package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.vo.procedure.P_RoomBoosterVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGiftVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGoodVo;
import com.dalbit.broadcast.vo.procedure.P_RoomShareLinkVo;
import com.dalbit.broadcast.vo.request.BoosterVo;
import com.dalbit.broadcast.vo.request.GiftVo;
import com.dalbit.broadcast.vo.request.GoodVo;
import com.dalbit.broadcast.vo.request.ShareLinkVo;
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

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomGoodVo apiData = new P_RoomGoodVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(goodVo.getRoomNo());

        String result = actionService.callBroadCastRoomGood(apiData, request);

        return result;
    }


    /**
     * 방송방 공유링크 확인
     */
    @GetMapping("/link")
    public String roomShareLink(@Valid ShareLinkVo shareLinkVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomShareLinkVo apiData = new P_RoomShareLinkVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setLinkCode(shareLinkVo.getLink());

        String result = actionService.callBroadCastShareLink(apiData);

        return result;
    }


    /**
     * 방송방 선물하기
     */
    @PostMapping("/gift")
    public String roomGift(@Valid GiftVo giftVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomGiftVo apiData = new P_RoomGiftVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(giftVo.getRoomNo());
        apiData.setGifted_mem_no(giftVo.getMemNo());
        apiData.setItem_no(giftVo.getItemNo());
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

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomBoosterVo apiData = new P_RoomBoosterVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(boosterVo.getRoomNo());
        apiData.setItem_no(boosterVo.getItemNo());
        apiData.setItem_cnt(boosterVo.getItemCnt());

        String result = actionService.callBroadCastRoomBooster(apiData, request);

        return result;

    }

}

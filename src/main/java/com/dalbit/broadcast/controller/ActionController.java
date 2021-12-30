package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.ActionService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
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
@RequestMapping("/broad")
@Scope("prototype")
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
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(goodVo.getRoomNo());

        String result = actionService.callBroadCastRoomGood(apiData, goodVo, request);

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
        apiData.setMem_no(MemberVo.getMyMemNo(request));
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
        if("index".equals(request.getParameter("mode"))){
            apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 99);
        }

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
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(giftVo.getRoomNo());
        apiData.setGifted_mem_no(giftVo.getMemNo());
        apiData.setItem_code(giftVo.getItemNo());
        apiData.setItem_cnt(giftVo.getItemCnt());
        apiData.setSecret("1".equals(giftVo.getIsSecret()) || "TRUE".equals(giftVo.getIsSecret().toUpperCase()) ? "1" : "0");
        apiData.setTtsText(giftVo.getTtsText());
        apiData.setActorId(giftVo.getActorId());

        String result = actionService.callBroadCastRoomGift(apiData, request);

        return result;
    }


    /**
     * 부스터 사용하기
     */
    @PostMapping("/boost")
    public String roomBooster(@Valid BoosterVo boosterVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomBoosterVo apiData = new P_RoomBoosterVo(boosterVo, request);
        String result = actionService.callBroadCastRoomBooster(apiData, boosterVo, request);

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


    /**
     * 방송방 채팅 얼리기
     */
    @PostMapping("/freeze")
    public String roomFreeze(@Valid FreezeVo freezeVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_FreezeVo apiData = new P_FreezeVo(freezeVo, request);
        String result = actionService.callRoomFreeze(apiData, request);
        return result;
    }


    /**
     * 보름달 띄우기 달 클릭
     */
    @GetMapping("/moon")
    public String moon(@Valid MoonVo moonVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MoonVo apiData = new P_MoonVo(moonVo, request);
        String result = actionService.callMoon(apiData);
        return result;
    }


    /**
     * 보름달 띄우기 체크 (서버용)
     */
    @GetMapping("/moon/check")
    public String moonCheck(@Valid MoonVo moonVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MoonCheckVo apiData = new P_MoonCheckVo(moonVo);
        String result = actionService.callMoonCheck(apiData, request);
        return result;
    }
}

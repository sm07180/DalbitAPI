package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.MiniGameService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
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
@RequestMapping("/broad/game")
@Scope("prototype")
public class MiniGameController {

    @Autowired
    MiniGameService miniGameService;

    /**
     * 미니게임 리스트 조회
     */
    @GetMapping("/list")
    public String miniGameList(@Valid MiniGameListVo miniGameListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameListVo apiData = new P_MiniGameListVo(miniGameListVo, request);
        return miniGameService.callMiniGameList(apiData);
    }

    /**
     * 미니게임 등록
     */
    @PostMapping("/add")
    public String miniGameAdd(@Valid MiniGameAddVo miniGameAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameAddVo apiData = new P_MiniGameAddVo(miniGameAddVo, request);
        return miniGameService.callMiniGameAdd(apiData, request);
    }

    /**
     * 미니게임 수정
     */
    @PostMapping("/edit")
    public String miniGameEdit(@Valid MiniGameEditVo miniGameEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameEditVo apiData = new P_MiniGameEditVo(miniGameEditVo, request);
        return miniGameService.callMiniGameEdit(apiData, request);
    }

    /**
     * 미니게임 불러오기
     */
    @GetMapping("/select")
    public String miniGameSelect(@Valid MiniGameVo miniGameVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameVo apiData = new P_MiniGameVo(miniGameVo, request);
        return miniGameService.callMiniGameSelect(apiData);
    }

    /**
     * 미니게임 시작
     */
    @PostMapping("/start")
    public String miniGameStart(@Valid MiniGameStartVo miniGameStartVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameStartVo apiData = new P_MiniGameStartVo(miniGameStartVo, request);
        return miniGameService.callMiniGameStart(apiData, request);
    }

    /**
     * 미니게임 종료
     */
    @PostMapping("/end")
    public String miniGameEnd(@Valid MiniGameEndVo miniGameEndVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_MiniGameEndVo apiData = new P_MiniGameEndVo(miniGameEndVo, request);
        return miniGameService.callMiniGameEnd(apiData, request);
    }

}

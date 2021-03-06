package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.WowzaService;
import com.dalbit.broadcast.vo.request.RoomCreateVo;
import com.dalbit.broadcast.vo.request.RoomJoinVo;
import com.dalbit.broadcast.vo.request.RoomTokenVo;
import com.dalbit.common.code.BroadcastStatus;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/broad/vw")
@Scope("prototype")
public class WowzaController {
    @Autowired
    WowzaService wowzaService;
    @Autowired
    GsonUtil gsonUtil;

    @RequestMapping("/broadcast/hook")
    public String wowzaHook(HttpServletRequest request){
        HashMap result = wowzaService.doUpdateWowzaState(request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status")));
    }

    @PostMapping("/create")
    public String doCreate(@Valid RoomCreateVo roomCreateVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //return gsonUtil.toJson(new JsonOutputVo(Status.생성금지, new RoomInfoVo()));

        HashMap result = wowzaService.doCreateBroadcast(roomCreateVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/join")
    public String doJoin(@Valid RoomJoinVo roomJoinVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        if("A000000000".equals(roomJoinVo.getRoomNo())){ //일반회원 참여시 따라가기 비공개일 경우
            return gsonUtil.toJson(new JsonOutputVo(BroadcastStatus.방송방조인따라가기비공개));
        }

        HashMap result = wowzaService.doJoinBroadcast(roomJoinVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/info")
    public String getBroadcast(@Valid RoomTokenVo roomTokenVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = wowzaService.getBroadcast(roomTokenVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/continue")
    public String doContinue(HttpServletRequest request) throws GlobalException {
        HashMap result = wowzaService.doContinueBroadcast(request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/normalize")
    public String doNormalize(HttpServletRequest request) throws GlobalException {
        HashMap result = wowzaService.doUpdateStateNormalize(request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }
}

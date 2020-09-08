package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.WowzaService;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/broad/vw")
public class WowzaController {
    @Autowired
    WowzaService wowzaService;
    @Autowired
    GsonUtil gsonUtil;

    @PostMapping("/broadcast/hook")
    public String wowzaHook(HttpServletRequest request){
        HashMap result = wowzaService.doUpdateWowzaState(request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status")));
    }

    @PostMapping("/create")
    public String doCreate(@Valid RoomCreateVo roomCreateVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = wowzaService.doCreateBroadcast(roomCreateVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/join")
    public String doJoin(@Valid RoomJoinVo roomJoinVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = wowzaService.doJoinBroadcast(roomJoinVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    @PostMapping("/info")
    public String getBroadcast(@Valid RoomTokenVo roomTokenVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        HashMap result = wowzaService.getBroadcast(roomTokenVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }
}

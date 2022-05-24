package com.dalbit.event.controller;

import com.dalbit.common.code.EventStatus;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.common.vo.PagingVo;
import com.dalbit.event.service.ElectricSignService;
import com.dalbit.event.vo.ElectricSignDJListVo;
import com.dalbit.event.vo.ElectricSignFanListVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/event/electric")
@Scope("prototype")
public class ElectricSignController {
    @Autowired
    ElectricSignService electricSignService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 전광판 이벤트 Dj 리스트
     */
    @GetMapping("/djList")
    public Object getElectricSignDjList(@Valid ElectricSignDJListVo electricSignDJListVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return electricSignService.getElectricSignDjList(electricSignDJListVo);
        } catch (Exception e) {
            log.error("ElectricSignController.java / getElectricSignDjList() => error: {}", e);
            HashMap resultMap = new HashMap();
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(0, 0, 0));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_실패, resultMap));
        }
    }

    /**
     * 전광판 이벤트 Dj 회원 정보
     */
    @GetMapping("/djSel")
    public Object getElectricSignDjSel(@Valid ElectricSignDJListVo electricSignDJListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return electricSignService.getElectricSignDjSel(electricSignDJListVo, request);
        } catch (Exception e) {
            log.error("ElectricSignController.java / getElectricSignDjSel() => error: {}, memNo: {}", e, MemberVo.getMyMemNo(request));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_디제이_조회_실패));
        }
    }

    /**
     * 전광판 이벤트 시청자 리스트
     */
    @GetMapping("/fanList")
    public Object getElectricSignFanList(@Valid ElectricSignFanListVo electricSignFanListVo, BindingResult bindingResult) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return electricSignService.getElectricSignFanList(electricSignFanListVo);
        } catch (Exception e) {
            log.error("ElectricSignController.java / getElectricSignFanList() => error: {}", e);
            HashMap resultMap = new HashMap();
            resultMap.put("list", new ArrayList());
            resultMap.put("paging", new PagingVo(0, 0, 0));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_실패, resultMap));
        }
    }

    /**
     * 전광판 이벤트 시청자 회원 정보
     */
    @GetMapping("/fanSel")
    public Object getElectricSignFanSel(@Valid ElectricSignFanListVo electricSignFanListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        try {
            return electricSignService.getElectricSignFanSel(electricSignFanListVo, request);
        } catch (Exception e) {
            log.error("ElectricSignController.java / getElectricSignFanSel() => error: {}, memNo", e, MemberVo.getMyMemNo(request));
            return gsonUtil.toJson(new JsonOutputVo(EventStatus.전광판_시청자_조회_실패));
        }
    }

}

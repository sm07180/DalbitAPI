package com.dalbit.search.controller;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.search.service.SearchService;
import com.dalbit.search.vo.procedure.P_LiveRoomSearchVo;
import com.dalbit.search.vo.procedure.P_MemberSearchVo;
import com.dalbit.search.vo.request.SearchVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("search")
public class SearchController {

    @Autowired
    SearchService searchService;

    /**
     * 회원 닉네임 검색
     */
    @GetMapping("member")
    public String memberNickSearch(@Valid SearchVo searchVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_MemberSearchVo apiData = new P_MemberSearchVo(searchVo, request);

        String result = searchService.callMemberNickSearch(apiData);

        return result;
    }

    /**
     * 라이브 방송 검색
     */
    @GetMapping("live")
    public String liveRoomSearch(@Valid SearchVo searchVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_LiveRoomSearchVo apiData = new P_LiveRoomSearchVo(searchVo, request);
        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setIsWowza(DalbitUtil.isWowza(deviceVo));

        String result = searchService.callLiveRoomSearch(apiData);

        return result;
    }
}

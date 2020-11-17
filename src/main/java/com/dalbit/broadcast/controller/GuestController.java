package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.GuestService;
import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.P_GuestManagementListVo;
import com.dalbit.broadcast.vo.request.GuestListVo;
import com.dalbit.broadcast.vo.request.GuestManagementVo;
import com.dalbit.common.service.CommonService;
import com.dalbit.exception.GlobalException;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/broad/guest")
/*

    streamName => wowza.prefix(DEV/REAL) + room_no + _ + mem_no
    var memNo : String
    var nickNm : String
    var rtmpOrigin : String
    var rtmpEdge : String
    var profImg : ImageData
    var webRtcUrl : String
    var webRtcAppName : String
    var webRtcStreamName : String
*/
public class GuestController {

    @Autowired
    GuestService guestService;
    @Autowired
    UserService userService;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;


    /**
     * 게스트 통합
     */
    @PostMapping("")
    public String guest(HttpServletRequest request) throws GlobalException {
        return guestService.guest(request);
    }


    /**
     * 게스트 관리
     */
    @GetMapping("/management")
    public String guestManagement(@Valid GuestManagementVo guestManagementVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_GuestManagementListVo apiData = new P_GuestManagementListVo(guestManagementVo, request);
        String result = guestService.callGuestManagementList(apiData, request);

        return result;
    }


    /**
     * 게스트 정보 리스트
     */
    @GetMapping("/list")
    public String guestList(@Valid GuestListVo guestListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String result = guestService.selectGuestList(guestListVo, request);
        return result;
    }
}

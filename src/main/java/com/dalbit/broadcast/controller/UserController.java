package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MypageBlackAddVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/broad")
@Scope("prototype")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;
    @Autowired
    MypageService mypageService;
    @Autowired
    GsonUtil gsonUtil;

    /**
     * 방송방 참여자 리스트
     */
    @GetMapping("/listeners")
    public String roomMemberList(@Valid JoinMemberListVo joinMemberListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int pageNo = DalbitUtil.isEmpty(joinMemberListVo.getPage()) ? 1 : joinMemberListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(joinMemberListVo.getRecords()) ? 10 : joinMemberListVo.getRecords();

        P_RoomMemberListVo apiData = new P_RoomMemberListVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(joinMemberListVo.getRoomNo());
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = userService.callBroadCastRoomMemberList(apiData, request);
        return result;
    }

    /**
     * #7853
     * 방송방 : 차단후 강퇴하기
     * */
    @PostMapping("/black/add")
    public String roomBlackAddKickOut(@Valid KickOutVo vo, HttpServletRequest request){
        try {
            P_MypageBlackAddVo param1 = new P_MypageBlackAddVo();
            P_RoomKickoutVo param2 = new P_RoomKickoutVo();

            // 차단
            param1.setBlack_mem_no(vo.getBlockNo());
            param1.setMem_no(MemberVo.getMyMemNo(request));

            // 강퇴
            param2.setMem_no(MemberVo.getMyMemNo(request));
            param2.setRoom_no(vo.getRoomNo());
            param2.setBlocked_mem_no(vo.getBlockNo());

            return userService.broadCastBlackAndKick(param1, param2, request);

        } catch (Exception e) {
            HashMap map = new HashMap();
            map.put("roomNo", vo.getRoomNo());
            map.put("reqMemNo", MemberVo.getMyMemNo(request));
            map.put("blockMemNo", vo.getBlockNo());

            log.error("UserController / roomBlackAddKickOut => Exception, param: {} \n Error: {}", gsonUtil.toJson(map) ,e);
            return gsonUtil.toJson(new JsonOutputVo(Status.강제퇴장_실패));
        }
    }

    /**
     * 방송방 강퇴하기
     */
    @PostMapping("/kickout")
    public String roomKickout(@Valid KickOutVo kickOutVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomKickoutVo apiData = new P_RoomKickoutVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(kickOutVo.getRoomNo());
        apiData.setBlocked_mem_no(kickOutVo.getBlockNo());

        String result = userService.callBroadCastRoomKickout(apiData, request);

        return result;
    }


    /**
     * 개인 정보 조회(방송생성, 참여 시)
     */
    @GetMapping("/profile")
    public String memberInfo(HttpServletRequest request){

        int memLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo(request));
        String result = userService.callMemberInfo(apiData);

        return result;
    }


    /**
     * 매니저 지정
     */
    @PostMapping("/manager")
    public String managerAdd(@Valid ManagerAddVo managerAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ManagerAddVo apiData = new P_ManagerAddVo(managerAddVo, request);
        String result = userService.callBroadCastRoomManagerAdd(apiData, request);

        return result;
    }


    /**
     * 매니저 취소
     */
    @DeleteMapping("/manager")
    public String managerDel(@Valid ManagerDelVo managerDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_ManagerDelVo apiData = new P_ManagerDelVo(managerDelVo, request);
        String result = userService.callBroadCastRoomManagerDel(apiData, request);

        return result;
    }


    /**
     * 방송방 팬 등록
     */
    @PostMapping("/fan")
    public String fanstarInsert(@Valid BroadFanstartInsertVo broadFanstartInsertVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_BroadFanstarInsertVo apiData = new P_BroadFanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo(request));
        apiData.setStar_mem_no(broadFanstartInsertVo.getMemNo());
        apiData.setRoom_no(broadFanstartInsertVo.getRoomNo());
        apiData.setType(broadFanstartInsertVo.getType());

        String result = userService.callFanstarInsert(apiData, request);

        return result;
    }

    /**
     * 방송방 팬 해제
     */
    @DeleteMapping("/fan")
    public String fanstarDelete(@Valid BroadFanstartDeleteVo broadFanstartDeleteVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_BroadFanstarDeleteVo apiData = new P_BroadFanstarDeleteVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo(request));
        apiData.setStar_mem_no(broadFanstartDeleteVo.getMemNo());
        apiData.setRoom_no(broadFanstartDeleteVo.getRoomNo());

        String result = userService.callFanstarDelete(apiData, request);

        return result;
    }
}

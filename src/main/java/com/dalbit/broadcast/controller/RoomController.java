package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/broad")
public class RoomController {

    @Autowired
    CommonService commonService;
    @Autowired
    RoomService roomService;
    @Autowired
    RestService restService;
    @Autowired
    GsonUtil gsonUtil;

    @Autowired
    MemberDao memberDao;


    /**
     * 방송방 나가기 (종료)
     */
    @DeleteMapping("/exit")
    public String roomExit(@Valid RoomExitVo roomExitVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomExitVo.getRoomNo());

        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setIp(deviceVo.getIp());
        apiData.setAppVersion(deviceVo.getAppVersion());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setIsHybrid(deviceVo.getIsHybrid());

        String result = roomService.callBroadCastRoomExit(apiData, request);

        return result;
    }


    @DeleteMapping("/exit/force")
    public String roomExitForce(@Valid RoomExitVo roomExitVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomExitVo.getRoomNo());

        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setIp(deviceVo.getIp());
        apiData.setAppVersion(deviceVo.getAppVersion());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setIsHybrid(deviceVo.getIsHybrid());

        String result = roomService.callBroadCastRoomExitForce(apiData, request);

        return result;
    }

    /**
     * 방송 정보수정
     */
    @PostMapping("/edit")
    public String roomEdit(@Valid RoomEditVo roomEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomEditVo apiData = new P_RoomEditVo(roomEditVo, request);
        String result = roomService.callBroadCastRoomEdit(apiData, request);

        return result;
    }


    /**
     * 방송방 리스트
     */
    @GetMapping(value = {"/list", "/list/bySocket"})
    public String roomList(@Valid RoomListVo roomListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RoomListVo apiData = new P_RoomListVo(roomListVo, request);
        String result = roomService.callBroadCastRoomList(apiData, request);

        return result;
    }

    /**
     * 방송방 현재 순위, 아이템 사용 현황 조회
     */
    @GetMapping("/boost")
    public String roomLiveRankInfo(@Valid LiveRankInfoVo liveRankInfoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomLiveRankInfoVo apiData = new P_RoomLiveRankInfoVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(liveRankInfoVo.getRoomNo());

        String result = roomService.callBroadCastRoomLiveRankInfo(apiData);

        return result;
    }


    /**
     * 방송방 현재 순위, 좋아요수 팬랭킹 조회
     */
    @GetMapping("/boost/end")
    public String roomCountInfo(@Valid LiveRankInfoVo liveRankInfoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomLiveRankInfoVo apiData = new P_RoomLiveRankInfoVo();
        apiData.setRoom_no(liveRankInfoVo.getRoomNo());

        String result = roomService.callBroadCastRoomChangeCount(apiData, request);

        return result;
    }

    /**
     * 방송방 선물받은 내역보기
     */
    @GetMapping("/history")
    public String roomGiftHistory(@Valid RoomGiftHistoryVo roomGiftHistoryVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        roomGiftHistoryVo.setPage(null);
        roomGiftHistoryVo.setRecords(null);

        P_RoomGiftHistoryVo apiData = new P_RoomGiftHistoryVo(roomGiftHistoryVo, request);
        String result = roomService.callBroadCastRoomGiftHistory(apiData);

        return result;
    }


    /**
     * 방송방 회원정보 조회
     */
    @GetMapping("/member/profile")
    public String roomMemberInfo(@Valid RoomMemberInfoVo roomMemberInfoVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomMemberInfoVo apiData = new P_RoomMemberInfoVo(roomMemberInfoVo, request);
        String result = roomService.callBroadCastRoomMemberInfo(apiData, request);
        return result;
    }

    /**
     * 방송방 토큰 재 생성
     */
    @PostMapping("/reToken")
    public String roomRefreshToken(@Valid RoomTokenVo roomTokenVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RoomStreamVo apiData = new P_RoomStreamVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomTokenVo.getRoomNo());

        return roomService.callBroadcastRoomStreamSelect(apiData, request);
    }

    /**
     * 방송방 토큰 재 생성 (스트리밍 아이디 포함)
     */
    @PostMapping("/refresh")
    public String roomRefreshAnt(@Valid RoomTokenVo roomTokenVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RoomStreamVo apiData = new P_RoomStreamVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomTokenVo.getRoomNo());

        return roomService.callAntRefresh(apiData, request);
    }

    /**
     * 방송방 상태 변경
     */
    @PostMapping("/state")
    public String roomStateUpdate(@Valid StateVo stateVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        return roomService.callBroadCastRoomStateUpate(stateVo, request);
    }

    /**
     * 회원 방송진행여부 체크
     */
    @GetMapping("/check")
    public String checkBroadcasting(HttpServletRequest request) {
        return roomService.callMemberBroadcastingCheck(request);
    }

    /**
     * 방송종료 요약 (청취차)
     */
    @GetMapping("/summary/listener")
    public String getSummaryListener(@Valid RoomExitVo roomExitVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap result = roomService.getSummaryListener(roomExitVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }

    /**
     * 방송종료 요약 (DJ)
     */
    @GetMapping("/summary/dj")
    public String getSummaryDj(@Valid RoomExitVo roomExitVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        HashMap result = roomService.getSummaryDj(roomExitVo, request);
        return gsonUtil.toJson(new JsonOutputVo((Status)result.get("status"), result.get("data")));
    }


    /**
     * 좋아요 내역
     */
    @GetMapping("/good/history")
    public String getGoodHistory(@Valid RoomGoodHistoryVo roomGoodHistoryVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());
        P_RoomGoodHistoryVo apiData = new P_RoomGoodHistoryVo(roomGoodHistoryVo, request);
        String result = roomService.getGoodHistory(apiData, request);
        return result;
    }
}
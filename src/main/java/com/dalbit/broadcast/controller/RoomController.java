package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
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


    /**
     * 방송방 생성
     */
    @PostMapping("/create")
    public String roomCreate(@Valid RoomCreateVo roomCreateVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        //토큰생성
        String streamId = (String) restService.antCreate(roomCreateVo.getTitle(), request).get("streamId");
        String publishToken = (String) restService.antToken(streamId, "publish", request).get("tokenId");
        String playToken = "";//(String) restService.antToken(streamId, "play", request).get("tokenId");
        log.info("bj_streamId: {}", streamId);
        log.info("bj_publishToken: {}", publishToken);
        log.info("bj_playToken: {}", playToken);

        P_RoomCreateVo apiData = new P_RoomCreateVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSubjectType(roomCreateVo.getRoomType());
        apiData.setTitle(roomCreateVo.getTitle());
        apiData.setBackgroundImage(roomCreateVo.getBgImg());
        apiData.setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomCreateVo.getBgImgRacy()));
        apiData.setWelcomMsg(roomCreateVo.getWelcomMsg());
        apiData.setNotice(roomCreateVo.getNotice());
        apiData.setEntryType(roomCreateVo.getEntryType());

        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setAppVersion(deviceVo.getAppVersion());

        apiData.setBj_streamid(streamId);
        apiData.setBj_publish_tokenid(publishToken);
        apiData.setBj_play_tokenid(playToken);

        String result = roomService.callBroadCastRoomCreate(apiData, request);

        return result;
    }


    /**
     * 방송방 참여하기
     */
    @PostMapping("/join")
    public String roomJoin(@Valid RoomJoinVo roomJoinVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        String roomNo = roomJoinVo.getRoomNo();

        //방참가를 위한 토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = new P_RoomJoinVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomNo);
        apiData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
        //apiData.setGuest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"));
        if(!DalbitUtil.isEmpty(apiData.getGuest_streamid())){
            apiData.setGuest_play_tokenid((String) restService.antToken(apiData.getGuest_streamid(), "play", request).get("tokenId"));
        }
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        //apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid((String) restService.antToken(apiData.getBj_streamid(), "play", request).get("tokenId"));
        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setIp(deviceVo.getIp());
        apiData.setAppVersion(deviceVo.getAppVersion());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setIsHybrid(deviceVo.getIsHybrid());


        String result = roomService.callBroadCastRoomJoin(apiData, request);

        return result;
    }


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

    /**
     * 방송 정보수정
     */
    @PostMapping("/edit")
    public String roomEdit(@Valid RoomEditVo roomEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomEditVo apiData = new P_RoomEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomEditVo.getRoomNo());
        apiData.setSubjectType(roomEditVo.getRoomType());
        apiData.setTitle(roomEditVo.getTitle());
        if(!DalbitUtil.isEmpty(roomEditVo.getBgImg())){
            apiData.setBackgroundImage(roomEditVo.getBgImg());
        }
        apiData.setBackgroundImageDelete(roomEditVo.getBgImgDel());
        apiData.setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomEditVo.getBgImgRacy()));
        apiData.setWelcomMsg(roomEditVo.getWelcomMsg());

        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setAppVersion(deviceVo.getAppVersion());

        String result = roomService.callBroadCastRoomEdit(apiData, request);

        return result;
    }


    /**
     * 방송방 리스트
     */
    @GetMapping("/list")
    public String roomList(@Valid RoomListVo roomListVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        int pageNo = DalbitUtil.isEmpty(roomListVo.getPage()) ? 1 : roomListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomListVo.getRecords()) ? 10 : roomListVo.getRecords();

        P_RoomListVo apiData = new P_RoomListVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setSubjectType(roomListVo.getRoomType());
        apiData.setSlctType(roomListVo.getSearchType());
        apiData.setGender(roomListVo.getGender());
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }


    /**
     * 방송방 정보조회
     */
    @GetMapping("/info")
    public String roomInfo(@Valid RoomInfo roomInfo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult, Thread.currentThread().getStackTrace()[1].getMethodName());

        P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
        apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo(request));
        apiData.setRoom_no(roomInfo.getRoomNo());

        String result = roomService.callBroadCastRoomInfoView(apiData, request);

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
        String result = roomService.callBroadCastRoomMemberInfo(apiData);
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
}
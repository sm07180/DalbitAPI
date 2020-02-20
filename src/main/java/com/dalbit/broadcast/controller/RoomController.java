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
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
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
public class RoomController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    GsonUtil gsonUtil;
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

        DalbitUtil.throwValidaionException(bindingResult);

        //토큰생성
        String streamId = (String) restService.antCreate(roomCreateVo.getTitle()).get("streamId");
        String publishToken = (String) restService.antToken(streamId, "publish").get("tokenId");
        String playToken = (String) restService.antToken(streamId, "play").get("tokenId");
        log.info("bj_streamId: {}", streamId);
        log.info("bj_publishToken: {}", publishToken);
        log.info("bj_playToken: {}", playToken);

        P_RoomCreateVo apiData = new P_RoomCreateVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
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

        String result = roomService.callBroadCastRoomCreate(apiData);

        return result;
    }


    /**
     * 방송방 참여하기
     */
    @PostMapping("/join")
    public String roomJoin(@Valid RoomJoinVo roomJoinVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        String roomNo = roomJoinVo.getRoomNo();

        //방참가를 위한 토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = new P_RoomJoinVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomNo);
        apiData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
        //apiData.setGuest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"));
        if(!DalbitUtil.isEmpty(apiData.getGuest_streamid())){
            apiData.setGuest_play_tokenid((String) restService.antToken(apiData.getGuest_streamid(), "play").get("tokenId"));
        }
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        //apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid((String) restService.antToken(apiData.getBj_streamid(), "play").get("tokenId"));
        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setOs(deviceVo.getOs());

        String result = roomService.callBroadCastRoomJoin(apiData);

        return result;
    }


    /**
     * 방송방 나가기 (종료)
     */
    @DeleteMapping("/exit")
    public String roomExit(@Valid RoomExitVo roomExitVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomExitVo.getRoomNo());

        String result = roomService.callBroadCastRoomExit(apiData);

        return result;
    }


    /**
     * 임의 삭제
     */
    @DeleteMapping("/exitForce")
    @Profile("local")
    public String roomDelete(HttpServletRequest request){
        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");
        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMemLogin(1);
        apiData.setMem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        apiData.setRoom_no(roomNo);

        String result = roomService.callBroadCastRoomExit(apiData);

        return result;
    }


    /**
     * 방송 정보수정
     */
    @PostMapping("/edit")
    public String roomEdit(@Valid RoomEditVo roomEditVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        String bgImg = roomEditVo.getBgImg();

        P_RoomEditVo apiData = new P_RoomEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomEditVo.getRoomNo());
        apiData.setSubjectType(roomEditVo.getRoomType());
        apiData.setTitle(roomEditVo.getTitle());
        if(!DalbitUtil.isEmpty(bgImg)){
            apiData.setBackgroundImage(bgImg);
        }
        apiData.setBackgroundImageDelete(roomEditVo.getBgImgDel());
        apiData.setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomEditVo.getBgImgRacy()));
        apiData.setWelcomMsg(roomEditVo.getWelcomMsg());

        DeviceVo deviceVo = new DeviceVo(request);
        apiData.setOs(deviceVo.getOs());
        apiData.setDeviceUuid(deviceVo.getDeviceUuid());
        apiData.setDeviceToken(deviceVo.getDeviceToken());
        apiData.setAppVersion(deviceVo.getAppVersion());

        String result = roomService.callBroadCastRoomEdit(apiData);

        return result;
    }


    /**
     * 방송방 리스트
     */
    @GetMapping("/list")
    public String roomList(@Valid RoomListVo roomListVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        int pageNo = DalbitUtil.isEmpty(roomListVo.getPage()) ? 1 : roomListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(roomListVo.getRecords()) ? 10 : roomListVo.getRecords();

        P_RoomListVo apiData = new P_RoomListVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setSubjectType(roomListVo.getRoomType());
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }


    /**
     * 방송 정보조회
     */
    @GetMapping("/info")
    public String roomInfo(@Valid RoomInfo roomInfo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomInfo.getRoomNo());

        String result = roomService.callBroadCastRoomInfoView(apiData);

        return result;

    }


    /**
     * 방송방 현재 순위, 아이템 사용 현황 조회
     */
    @GetMapping("/boost")
    public String roomLiveRankInfo(@Valid LiveRankInfoVo liveRankInfoVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomLiveRankInfoVo apiData = new P_RoomLiveRankInfoVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(liveRankInfoVo.getRoomNo());

        String result = roomService.callBroadCastRoomLiveRankInfo(apiData);

        return result;
    }


    /**
     * 방송방 선물받은 내역보기
     */
    @GetMapping("/history")
    public String roomGiftHistory(@Valid RoomGiftHistoryVo roomGiftHistoryVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomGiftHistoryVo apiData = new P_RoomGiftHistoryVo(roomGiftHistoryVo);
        String result = roomService.callBroadCastRoomGiftHistory(apiData);

        return result;
    }


    /**
     * 방송방 회원정보 조회
     */
    @GetMapping("/member/profile")
    public String roomMemberInfo(@Valid RoomMemberInfoVo roomMemberInfoVo, BindingResult bindingResult) throws GlobalException{
        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomMemberInfoVo apiData = new P_RoomMemberInfoVo(roomMemberInfoVo);
        String result = roomService.callBroadCastRoomMemberInfo(apiData);
        return result;
    }

    /**
     * 방송방 토큰 재 생성
     */
    @PostMapping("/reToken")
    public String roomRefreshToken(@Valid RoomTokenVo roomTokenVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);
        P_RoomStreamVo apiData = new P_RoomStreamVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomTokenVo.getRoomNo());

        return roomService.callBroadcastRoomStreamSelect(apiData);
    }

    /**
     * 방송방 상태 변경
     */
    @PostMapping("state")
    public String roomStateUpdate(@Valid StateVo stateVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);
        P_RoomStateUpdateVo apiData = new P_RoomStateUpdateVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(stateVo.getRoomNo());
        apiData.setState(stateVo.getState());

        return roomService.callBroadCastRoomStateUpate(apiData);
    }
}
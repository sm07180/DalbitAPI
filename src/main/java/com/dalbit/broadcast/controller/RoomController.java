package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


@Slf4j
@RestController
@RequestMapping("/broad")
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
    public String roomCreate(HttpServletRequest request) throws GlobalException {
        //토큰생성
        String streamId = (String) restService.antCreate(DalbitUtil.convertRequestParamToString(request, "title")).get("streamId");
        String publishToken = (String) restService.antToken(streamId, "publish").get("tokenId");
        String playToken = (String) restService.antToken(streamId, "play").get("tokenId");
        log.info("bj_streamId: {}", streamId);
        log.info("bj_publishToken: {}", publishToken);
        log.info("bj_playToken: {}", playToken);

        String bgImg = DalbitUtil.convertRequestParamToString(request, "bgImg");
        int bgGrade = DalbitUtil.convertRequestParamToInteger(request, "bgImgRacy") < 0 ? 0 : DalbitUtil.convertRequestParamToInteger(request, "bgImgRacy");

        if(DalbitUtil.isEmpty(bgImg)){
            bgImg = "/default/roombg_" + (DalbitUtil.randomValue("number", 1)) + ".jpg";
        }else{
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        P_RoomCreateVo apiData = new P_RoomCreateVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "roomType"));
        apiData.setTitle(DalbitUtil.convertRequestParamToString(request, "title"));
        apiData.setBackgroundImage(bgImg);
        apiData.setBackgroundImageGrade(bgGrade);
        apiData.setWelcomMsg(DalbitUtil.convertRequestParamToString(request,"welcomMsg"));
        apiData.setNotice(DalbitUtil.convertRequestParamToString(request,"notice"));
        apiData.setEntryType(DalbitUtil.convertRequestParamToInteger(request,"entryType"));
        apiData.setOs(DalbitUtil.convertRequestParamToInteger(request,"os"));
        apiData.setDeviceUuid(DalbitUtil.convertRequestParamToString(request, "deviceId"));
        apiData.setDeviceToken(DalbitUtil.convertRequestParamToString(request, "deviceToken"));
        apiData.setAppVersion(DalbitUtil.convertRequestParamToString(request, "appVer"));
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
    public String roomJoin(HttpServletRequest request) throws GlobalException{
        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");

        //방참가를 위한 토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = new P_RoomJoinVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomNo);
        apiData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
        apiData.setGuest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"));
        apiData.setGuest_play_tokenid(DalbitUtil.getStringMap(resultMap,"guest_play_tokenid"));
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid(DalbitUtil.getStringMap(resultMap,"bj_play_tokenid"));

        String result = roomService.callBroadCastRoomJoin(apiData);

        return result;
    }

    /**
     * 방송방 나가기 (종료)
     */
    @DeleteMapping("/exit")
    public String roomExit(HttpServletRequest request){
        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");
        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomNo);

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
    public String roomEdit(HttpServletRequest request){

        String bgImg = DalbitUtil.convertRequestParamToString(request, "bgImg");
        int bgGrade = DalbitUtil.convertRequestParamToInteger(request, "bgImgRacy") < 0 ? 0 : DalbitUtil.convertRequestParamToInteger(request, "bgImgRacy");

        P_RoomEditVo apiData = new P_RoomEditVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "roomType"));
        apiData.setTitle(DalbitUtil.convertRequestParamToString(request, "title"));
        if(!DalbitUtil.isEmpty(bgImg)){
            bgImg = DalbitUtil.replacePath(bgImg);
            apiData.setBackgroundImage(bgImg);
        }
        //apiData.setBackgroundImageDelete(DalbitUtil.convertRequestParamToString(request, "bgImgDel"));
        apiData.setBackgroundImageGrade(bgGrade);
        apiData.setWelcomMsg(DalbitUtil.convertRequestParamToString(request, "welcomMsg"));
        apiData.setOs(DalbitUtil.convertRequestParamToInteger(request,"os"));
        apiData.setDeviceUuid(DalbitUtil.convertRequestParamToString(request, "deviceId"));
        apiData.setDeviceToken(DalbitUtil.convertRequestParamToString(request, "deviceToken"));
        apiData.setAppVersion(DalbitUtil.convertRequestParamToString(request, "appVer"));

        String result = roomService.callBroadCastRoomEdit(apiData);

        return result;
    }

    /**
     * 방송방 리스트
     */
    @GetMapping("/list")
    public String roomList(HttpServletRequest request){

        int pageNo = (DalbitUtil.convertRequestParamToInteger(request, "page")) == -1 ? 1 : DalbitUtil.convertRequestParamToInteger(request, "page");
        int pageCnt = (DalbitUtil.convertRequestParamToInteger(request, "records")) == -1 ? 5 : DalbitUtil.convertRequestParamToInteger(request, "records");

        P_RoomListVo apiData = new P_RoomListVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "roomType"));
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }


    /**
     * 방송 정보조회
     */
    @GetMapping("/info")
    public String roomInfo(HttpServletRequest request){

        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");

        P_RoomInfoViewVo apiData = new P_RoomInfoViewVo();
        apiData.setMemLogin(DalbitUtil.isLogin() ? 1 : 0);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(roomNo);

        String result = roomService.callBroadCastRoomInfoView(apiData);

        return result;

    }

}


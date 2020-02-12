package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.P_RoomGuestAddVo;
import com.dalbit.broadcast.vo.procedure.P_RoomGuestDeleteVo;
import com.dalbit.broadcast.vo.procedure.P_RoomKickoutVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.service.ProfileService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/broad")
public class UserController {

    @Autowired
    MessageUtil messageUtil;
    @Autowired
    UserService userService;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;
    @Autowired
    ProfileService profileService;

    /**
     * 방송방 참여자 리스트
     */
    @GetMapping("/listeners")
    public String roomMemberList(HttpServletRequest request){

        int pageNo = (DalbitUtil.convertRequestParamToInteger(request, "page")) == -1 ? 1 : DalbitUtil.convertRequestParamToInteger(request, "page");
        int pageCnt = (DalbitUtil.convertRequestParamToInteger(request, "records")) == -1 ? 5 : DalbitUtil.convertRequestParamToInteger(request, "records");

        P_RoomMemberListVo apiData = new P_RoomMemberListVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = userService.callBroadCastRoomMemberList(apiData);

        return result;
    }

    /**
     * 게스트 지정하기
     */
    @PostMapping("/guest")
    public String roomGuestAdd(HttpServletRequest request) throws GlobalException {
        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");
        //게스트 지정을 위한 BJ토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        //Guest 토큰생성
        String guestStreamId = (String) restService.antCreate(DalbitUtil.convertRequestParamToString(request, "title")).get("streamId");
        String guestPublishToken = (String) restService.antToken(guestStreamId, "publish").get("tokenId");
        String guestPlayToken = (String) restService.antToken(guestStreamId, "play").get("tokenId");

        log.info("guest_streamid: {}", guestStreamId);
        log.info("guest_publish_tokenid: {}", guestPublishToken);
        log.info("guest_play_tokenid: {}", guestPlayToken);

        P_RoomGuestAddVo apiData = new P_RoomGuestAddVo();
        apiData.setRoom_no(roomNo);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setGuest_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        apiData.setGuest_streamid(guestStreamId);
        apiData.setGuest_publish_tokenid(guestPublishToken);
        apiData.setGuest_play_tokenid(guestPlayToken);
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid(DalbitUtil.getStringMap(resultMap, "bj_playToken"));

        String result = userService.callBroadCastRoomGuestAdd(apiData);

        return result;
    }


    /**
     * 게스트 취소하기
     */
    @DeleteMapping("/guest")
    public String roomGuestDelete(HttpServletRequest request) throws GlobalException{

        String roomNo = DalbitUtil.convertRequestParamToString(request, "roomNo");

        //게스트 취소를 위한 토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomGuestDeleteVo apiData = new P_RoomGuestDeleteVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setGuest_mem_no(DalbitUtil.convertRequestParamToString(request, "memNo"));
        apiData.setRoom_no(roomNo);
        apiData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
        apiData.setGuest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"));
        apiData.setGuest_play_tokenid(DalbitUtil.getStringMap(resultMap,"guest_play_tokenid"));
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid(DalbitUtil.getStringMap(resultMap,"bj_play_tokenid"));

        String result = userService.callBroadCastRoomGuestDelete(apiData);

        return  result;
    }

    /**
     * 방송방 강퇴하기
     */
    @PostMapping("/kickout")
    public String roomKickout(HttpServletRequest request){
        P_RoomKickoutVo apiData = new P_RoomKickoutVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "roomNo"));
        apiData.setBlocked_mem_no(DalbitUtil.convertRequestParamToString(request, "blockNo"));

        String result = userService.callBroadCastRoomKickout(apiData);

        return result;
    }

    /**
     * 개인 정보 조회(방송생성, 참여 시)
     */
    @GetMapping("/profile")
    public String memberInfo(HttpServletRequest request){
        int memLogin = DalbitUtil.isLogin() ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo());
        String result = userService.callMemberInfo(apiData);
        return result;
    }

    /* #################### 여기까지 API명세서 기준 작업완료 ######################## */


    /**
     * 매니저지정
     */
    @PostMapping("/{brodNo}/mgr/{memNo}")
    public String addManager(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.매니저지정, data)));
    }

    /**
     * 매니저해제
     */
    @DeleteMapping("/{brodNo}/mgr/{memNo}")
    public String deleteManager(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트초대
     */
    @PostMapping("/{brodNo}/gst/{memNo}/invite")
    public String inviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트초대, data)));
    }

    /**
     * 게스트초대취소
     */
    @DeleteMapping("/{brodNo}/gst/{memNo}/invite")
    public String deleteInviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트초대수락
     */
    @PostMapping("/{brodNo}/gst")
    public String joinGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트초대수락, data)));
    }

    /**
     * 게스트신청
     */
    @PostMapping("/{brodNo}/gst/{memNo}/apply")
    public String applyGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.게스트신청, data)));
    }

    /**
     * 게스트해제
     */
    @DeleteMapping("/{brodNo}/gst")
    public String deleteGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }

    /**
     * 게스트나가기
     */
    @DeleteMapping("/{brodNo}/gst/out")
    public String outGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.삭제, data)));
    }


    /* ######################## Native 연동에서만 필요한 부분 ########################## */
    @Profile({"local", "dev"})
    @GetMapping("devBroad/bj")
    public String getDevBjRoom(HttpServletRequest request){
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, userService.getDevBjRoom(MemberVo.getMyMemNo()))));
    }

    @Profile({"local", "dev"})
    @GetMapping("devBroad/join")
    public String getDevJoinRoom(HttpServletRequest request){
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, userService.getDevJoinRoom(MemberVo.getMyMemNo()))));
    }
}

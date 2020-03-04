package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.UserService;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_ProfileInfoVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;

@Slf4j
@RestController
@RequestMapping("/broad")
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    RestService restService;
    @Autowired
    CommonService commonService;


    /**
     * 방송방 참여자 리스트
     */
    @GetMapping("/listeners")
    public String roomMemberList(@Valid JoinMemberListVo joinMemberListVo, BindingResult bindingResult) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        int pageNo = DalbitUtil.isEmpty(joinMemberListVo.getPage()) ? 1 : joinMemberListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(joinMemberListVo.getRecords()) ? 10 : joinMemberListVo.getRecords();

        P_RoomMemberListVo apiData = new P_RoomMemberListVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(joinMemberListVo.getRoomNo());
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = userService.callBroadCastRoomMemberList(apiData);

        return result;
    }


    /**
     * 게스트 지정하기
     */
    @PostMapping("/guest")
    public String roomGuestAdd(@Valid GuestAddVo guestAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException {

        DalbitUtil.throwValidaionException(bindingResult);

        String roomNo = guestAddVo.getRoomNo();
        //게스트 지정을 위한 BJ토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        //Guest 토큰생성
        String guestStreamId = (String) restService.antCreate(guestAddVo.getTitle()).get("streamId");
        String guestPublishToken = (String) restService.antToken(guestStreamId, "publish").get("tokenId");
        String guestPlayToken = (String) restService.antToken(guestStreamId, "play").get("tokenId");

        log.info("guest_streamid: {}", guestStreamId);
        log.info("guest_publish_tokenid: {}", guestPublishToken);
        log.info("guest_play_tokenid: {}", guestPlayToken);

        P_RoomGuestAddVo apiData = new P_RoomGuestAddVo();
        apiData.setRoom_no(roomNo);
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setGuest_mem_no(guestAddVo.getMemNo());
        apiData.setGuest_streamid(guestStreamId);
        apiData.setGuest_publish_tokenid(guestPublishToken);
        apiData.setGuest_play_tokenid(guestPlayToken);
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid((String) restService.antToken(apiData.getBj_streamid(), "play").get("tokenId"));

        String result = userService.callBroadCastRoomGuestAdd(apiData, request);

        return result;
    }


    /**
     * 게스트 취소하기
     */
    @DeleteMapping("/guest")
    public String roomGuestDelete(@Valid GuestDelVo guestDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        String roomNo = guestDelVo.getRoomNo();
        //게스트 취소를 위한 토큰 조회
        HashMap resultMap = commonService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomGuestDeleteVo apiData = new P_RoomGuestDeleteVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setGuest_mem_no(guestDelVo.getMemNo());
        apiData.setRoom_no(roomNo);
        apiData.setGuest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"));
        apiData.setGuest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"));
        apiData.setGuest_play_tokenid(DalbitUtil.getStringMap(resultMap,"guest_play_tokenid"));
        apiData.setBj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"));
        apiData.setBj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"));
        apiData.setBj_play_tokenid(DalbitUtil.getStringMap(resultMap,"bj_play_tokenid"));

        String result = userService.callBroadCastRoomGuestDelete(apiData, request);

        return  result;
    }


    /**
     * 방송방 강퇴하기
     */
    @PostMapping("/kickout")
    public String roomKickout(@Valid KickOutVo kickOutVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_RoomKickoutVo apiData = new P_RoomKickoutVo();
        apiData.setMem_no(MemberVo.getMyMemNo());
        apiData.setRoom_no(kickOutVo.getRoomNo());
        apiData.setBlocked_mem_no(kickOutVo.getBlockNo());

        String result = userService.callBroadCastRoomKickout(apiData, request);

        return result;
    }


    /**
     * 개인 정보 조회(방송생성, 참여 시)
     */
    @GetMapping("/profile")
    public String memberInfo(){
        int memLogin = DalbitUtil.isLogin() ? 1 : 0;
        P_ProfileInfoVo apiData = new P_ProfileInfoVo(memLogin, MemberVo.getMyMemNo());
        String result = userService.callMemberInfo(apiData);

        return result;
    }


    /**
     * 매니저 지정
     */
    @PostMapping("/manager")
    public String managerAdd(@Valid ManagerAddVo managerAddVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_ManagerAddVo apiData = new P_ManagerAddVo(managerAddVo);
        String result = userService.callBroadCastRoomManagerAdd(apiData, request);

        return result;
    }


    /**
     * 매니저 취소
     */
    @DeleteMapping("/manager")
    public String managerDel(@Valid ManagerDelVo managerDelVo, BindingResult bindingResult, HttpServletRequest request) throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_ManagerDelVo apiData = new P_ManagerDelVo(managerDelVo);
        String result = userService.callBroadCastRoomManagerDel(apiData, request);

        return result;
    }


    /**
     * 방송방 팬 등록
     */
    @PostMapping("/fan")
    public String fanstarInsert(@Valid BroadFanstartInsertVo broadFanstartInsertVo, BindingResult bindingResult, HttpServletRequest request)throws GlobalException{

        DalbitUtil.throwValidaionException(bindingResult);

        P_BroadFanstarInsertVo apiData = new P_BroadFanstarInsertVo();
        apiData.setFan_mem_no(MemberVo.getMyMemNo());
        apiData.setStar_mem_no(broadFanstartInsertVo.getMemNo());
        apiData.setRoom_no(broadFanstartInsertVo.getRoomNo());

        String result = userService.callFanstarInsert(apiData, request);

        return result;
    }


    /* #################### 여기까지 API명세서 기준 작업완료 ######################## */


    /**
     * 게스트초대
     */
    @PostMapping("/{brodNo}/gst/{memNo}/invite")
    public String inviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.게스트초대, data));
    }

    /**
     * 게스트초대취소
     */
    @DeleteMapping("/{brodNo}/gst/{memNo}/invite")
    public String deleteInviteGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.삭제, data));
    }

    /**
     * 게스트초대수락
     */
    @PostMapping("/{brodNo}/gst")
    public String joinGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.게스트초대수락, data));
    }

    /**
     * 게스트신청
     */
    @PostMapping("/{brodNo}/gst/{memNo}/apply")
    public String applyGuest(@PathVariable String brodNo, @PathVariable String memNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.게스트신청, data));
    }

    /**
     * 게스트해제
     */
    @DeleteMapping("/{brodNo}/gst")
    public String deleteGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.삭제, data));
    }

    /**
     * 게스트나가기
     */
    @DeleteMapping("/{brodNo}/gst/out")
    public String outGuest(@PathVariable String brodNo){

        HashMap data = new HashMap();

        return new Gson().toJson(new JsonOutputVo(Status.삭제, data));
    }


    /* ######################## Native 연동에서만 필요한 부분 ########################## */
    @Profile({"local", "dev"})
    @GetMapping("devBroad/bj")
    public String getDevBjRoom(HttpServletRequest request){
        return new Gson().toJson(new JsonOutputVo(Status.조회, userService.getDevBjRoom(MemberVo.getMyMemNo())));
    }

    @Profile({"local", "dev"})
    @GetMapping("devBroad/join")
    public String getDevJoinRoom(HttpServletRequest request){
        return new Gson().toJson(new JsonOutputVo(Status.조회, userService.getDevJoinRoom(MemberVo.getMyMemNo())));
    }


    @Profile({"local", "dev"})
    @GetMapping("devBroad/disconnect")
    public String selectDisconnectRoom(HttpServletRequest request){
        return new Gson().toJson(new JsonOutputVo(Status.조회, userService.selectDisconnectRoom(MemberVo.getMyMemNo())));
    }

    @Profile({"local", "dev"})
    @PostMapping("devBroad/normal")
    public String updateNormalRoom(HttpServletRequest request){
        userService.updateNormalRoom(request.getParameter("roomNo"));
        return new Gson().toJson(new JsonOutputVo(Status.조회));
    }
}

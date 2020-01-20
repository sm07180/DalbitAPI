package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.P_RoomGuestAddVo;
import com.dalbit.broadcast.vo.P_RoomMemberListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.JsonOutputVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.MessageUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    RoomService roomService;
    @Autowired
    RestService restService;

    /**
     * 방송방 참여자 리스트
     */
    @GetMapping("/listeners")
    public String roomMemberList(HttpServletRequest request){

        int pageNo = (DalbitUtil.convertRequestParamToInteger(request, "i_page")) == -1 ? 1 : DalbitUtil.convertRequestParamToInteger(request, "i_page");
        int pageCnt = (DalbitUtil.convertRequestParamToInteger(request, "i_records")) == -1 ? 5 : DalbitUtil.convertRequestParamToInteger(request, "i_records");

        P_RoomMemberListVo apiData = P_RoomMemberListVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(DalbitUtil.convertRequestParamToString(request, "s_room_no"))
                .pageNo(pageNo)
                .pageCnt(pageCnt)
                .build();

        String result = roomService.callBroadCastRoomMemberList(apiData);

        return result;
    }

    /**
     * 게스트 지정하기
     */
    @PostMapping("/guest")
    public String roomGuestAdd(HttpServletRequest request) throws GlobalException {
        String roomNo = DalbitUtil.convertRequestParamToString(request, "s_room_no");
        //게스트 지정을 위한 BJ토큰 조회
        HashMap resultMap = roomService.callBroadCastRoomStreamIdRequest(roomNo);

        //Guest 토큰생성
        String guestStreamId = (String) restService.antCreate(DalbitUtil.convertRequestParamToString(request, "s_title")).get("streamId");
        String guestPublishToken = (String) restService.antToken(guestStreamId, "publish").get("tokenId");
        String guestPlayToken = (String) restService.antToken(guestStreamId, "play").get("tokenId");

        log.info("guest_streamid: {}", guestStreamId);
        log.info("guest_publish_tokenid: {}", guestPublishToken);
        log.info("guest_play_tokenid: {}", guestPlayToken);


        P_RoomGuestAddVo apiData = P_RoomGuestAddVo.builder()
                .mem_no(DalbitUtil.convertRequestParamToString(request, "s_mem_no"))
                .room_no(roomNo)
                .guest_streamid(guestStreamId)
                .guest_publish_tokenid(guestPublishToken)
                .guest_play_tokenid(guestPlayToken)
                .bj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"))
                .bj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"))
                .bj_play_tokenid(DalbitUtil.getStringMap(resultMap, "bj_playToken"))
                .build();

        String result = roomService.callBroadCastRoomGuestAdd(apiData);

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
}

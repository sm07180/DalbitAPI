package com.dalbit.broadcast.controller;

import com.dalbit.broadcast.service.RoomService;
import com.dalbit.broadcast.vo.*;
import com.dalbit.common.code.Status;
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
    RoomService roomService;
    @Autowired
    RestService restService;
    @Value("${server.img.url}")
    private String IMG_URL;


    /**
     * 방송방 생성
     */
    @PostMapping("/create")
    public String roomCreate(HttpServletRequest request) throws GlobalException {
        //토큰생성
        String streamId = (String) restService.antCreate(DalbitUtil.convertRequestParamToString(request, "s_title")).get("streamId");
        String publishToken = (String) restService.antToken(streamId, "publish").get("tokenId");
        String playToken = (String) restService.antToken(streamId, "play").get("tokenId");
        log.info("bj_streamId: {}", streamId);
        log.info("bj_publishToken: {}", publishToken);
        log.info("bj_playToken: {}", playToken);

        P_RoomCreateVo apiData = P_RoomCreateVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .subjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"))
                .title(DalbitUtil.convertRequestParamToString(request, "s_title"))
                .backgroundImage(IMG_URL+"/"+DalbitUtil.convertRequestParamToString(request, "s_bgImg"))
                .backgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request, "i_bgRacy"))
                .welcomMsg(DalbitUtil.convertRequestParamToString(request,"s_welcome"))
                .notice(DalbitUtil.convertRequestParamToString(request,"s_notice"))
                .entry(DalbitUtil.convertRequestParamToInteger(request,"i_entry"))
                .age(DalbitUtil.convertRequestParamToInteger(request,"i_age"))
                .os(DalbitUtil.convertRequestParamToInteger(request,"i_os"))
                .bj_streamid(streamId)
                .bj_publish_tokenid(publishToken)
                .bj_play_tokenid(playToken)
                .build();

        String result = roomService.callBroadCastRoomCreate(apiData);

        return result;
    }

    /**
     * 방송방 참여하기
     */
    @PostMapping("/join")
    public String roomJoin(HttpServletRequest request) throws GlobalException{
        String roomNo = DalbitUtil.convertRequestParamToString(request, "s_room_no");

        //방참가를 위한 토큰 조회
        HashMap resultMap = roomService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = P_RoomJoinVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(roomNo)
                .guest_streamid(DalbitUtil.getStringMap(resultMap,"guest_streamid"))
                .guest_publish_tokenid(DalbitUtil.getStringMap(resultMap,"guest_publish_tokenid"))
                .guest_play_tokenid(DalbitUtil.getStringMap(resultMap,"guest_play_tokenid"))
                .bj_streamid(DalbitUtil.getStringMap(resultMap,"bj_streamid"))
                .bj_publish_tokenid(DalbitUtil.getStringMap(resultMap,"bj_publish_tokenid"))
                .bj_play_tokenid(DalbitUtil.getStringMap(resultMap,"bj_play_tokenid"))
                .build();

        String result = roomService.callBroadCastRoomJoin(apiData);

        return result;
    }

    /**
     * 방송방 나가기
     */
    @PostMapping("/exit")
    public String roomExit(HttpServletRequest request){
        String roomNo = DalbitUtil.convertRequestParamToString(request, "s_room_no");
        P_RoomExitVo apiData = P_RoomExitVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(roomNo)
                .build();

        String result = roomService.callBroadCastRoomExit(apiData);

        return result;
    }

    /**
     * 방송 정보수정
     */
    @PostMapping("/edit")
    public String roomEdit(HttpServletRequest request){

        //TODO-방송 정보 조회 ? 서버? ...

        P_RoomEditVo apiData = P_RoomEditVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(DalbitUtil.convertRequestParamToString(request, "s_room_no"))
                .subjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"))
                .title(DalbitUtil.convertRequestParamToString(request, "s_title"))
                .backgroundImage(DalbitUtil.convertRequestParamToString(request, "s_bgImg"))
                .backgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request, "i_bgRacy"))
                .welcomMsg(DalbitUtil.convertRequestParamToString(request, "s_welcome"))
                .entry(DalbitUtil.convertRequestParamToInteger(request, "i_entry"))
                .age(DalbitUtil.convertRequestParamToInteger(request, "i_age"))
                .build();

        String result = roomService.callBroadCastRoomEdit(apiData);

        return result;
    }

    /**
     * 방송방 리스트
     */
    @GetMapping("/list")
    public String roomList(HttpServletRequest request){

        int pageNo = (DalbitUtil.convertRequestParamToInteger(request, "i_page")) == -1 ? 1 : DalbitUtil.convertRequestParamToInteger(request, "i_page");
        int pageCnt = (DalbitUtil.convertRequestParamToInteger(request, "i_records")) == -1 ? 5 : DalbitUtil.convertRequestParamToInteger(request, "i_records");

        P_RoomListVo apiData = P_RoomListVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .subjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"))
                .pageNo(pageNo)
                .pageCnt(pageCnt)
                .build();

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }

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
     * 방송방 좋아요 추가
     */
    @PostMapping("/likes")
    public String roomGood(HttpServletRequest request){

        P_RoomGoodVo adiData = P_RoomGoodVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(DalbitUtil.convertRequestParamToString(request, "s_room_no"))
                .build();

        String result = roomService.callBroadCastRoomGood(adiData);

        return result;
    }


    /**
     * 게스트 지정하기
     */
    @PostMapping("/guest")
    public String roomGuestAdd(HttpServletRequest request) throws GlobalException{
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


    /**
     * 방송종료
     */
    @DeleteMapping("/{brodNo}")
    public String deleteBrod(@PathVariable String brodNo ){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송종료, data)));
    }


    /**
     * 방송 정보조회
     */
    @GetMapping("/{brodNo}")
    public String getBrod(@PathVariable String brodNo){

        HashMap data = new HashMap();
        data.put("gubun", "BROD00100");
        data.put("gubunNM", "일상");
        data.put("title", "방 제목입니다.이렇게!");
        data.put("intro", "안녕하세요! \n 제 방송 즐겁게 즐기다 가세요!");
        data.put("isFan", true);
        data.put("isOver20", false);

        HashMap bgImg = new HashMap();
        bgImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        bgImg.put("path","/2019/12/05/15");
        bgImg.put("name","1231567454123.jpg");

        data.put("bgImg", bgImg);

        HashMap owner = new HashMap();
        owner.put("memNo", "M000125");
        owner.put("nickNm", "방장닉네임");

        HashMap ownerImg = new HashMap();
        ownerImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        ownerImg.put("path","/2019/12/05/15");
        ownerImg.put("name","1231567454123.jpg");

        owner.put("img", ownerImg);
        data.put("owner", owner);

        HashMap guest = new HashMap();
        guest.put("memNo", "M000125");
        guest.put("nickNm", "게스트닉네임");

        HashMap guestImg = new HashMap();
        guestImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        guestImg.put("path","/2019/12/05/15");
        guestImg.put("name","1231567454123.jpg");

        guest.put("img", guestImg);
        data.put("guest", guest);

        HashMap managers = new HashMap();
        managers.put("guest", "매니저정보");
        managers.put("memNo", "M000125");
        managers.put("nickNm", "매니저닉네임");

        HashMap managersImg = new HashMap();
        managersImg.put("url","https://photo.wawatoc.com/2019/12/05/15/1231567454123.jpg");
        managersImg.put("path","/2019/12/05/15");
        managersImg.put("name","1231567454123.jpg");

        managers.put("img", managersImg);
        data.put("managers", managers);

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.조회, data)));
    }

}


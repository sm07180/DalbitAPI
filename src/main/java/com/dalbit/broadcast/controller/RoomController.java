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

        P_RoomCreateVo apiData = new P_RoomCreateVo();
        apiData.setMem_no(MemberVo.getUserInfo().getMem_no());
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"));
        apiData.setTitle(DalbitUtil.convertRequestParamToString(request, "s_title"));
        apiData.setBackgroundImage(IMG_URL+"/"+DalbitUtil.convertRequestParamToString(request, "s_bgImg"));
        apiData.setBackgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request, "i_bgRacy"));
        apiData.setWelcomMsg(DalbitUtil.convertRequestParamToString(request,"s_welcome"));
        apiData.setNotice(DalbitUtil.convertRequestParamToString(request,"s_notice"));
        apiData.setEntry(DalbitUtil.convertRequestParamToInteger(request,"entryType") == 1 ? 1: 0);
        apiData.setAge(DalbitUtil.convertRequestParamToInteger(request,"entryType") == 2 ? 1 : 0);
        apiData.setOs(DalbitUtil.convertRequestParamToInteger(request,"i_os"));
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
        String roomNo = DalbitUtil.convertRequestParamToString(request, "s_room_no");

        //방참가를 위한 토큰 조회
        HashMap resultMap = roomService.callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = new P_RoomJoinVo();
        apiData.setMem_no(MemberVo.getUserInfo().getMem_no());
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
     * 방송방 나가기
     */
    @PostMapping("/exit")
    public String roomExit(HttpServletRequest request){
        String roomNo = DalbitUtil.convertRequestParamToString(request, "s_room_no");
        P_RoomExitVo apiData = new P_RoomExitVo();
        apiData.setMem_no(MemberVo.getUserInfo().getMem_no());
        apiData.setRoom_no(roomNo);

        String result = roomService.callBroadCastRoomExit(apiData);

        return result;
    }

    /**
     * 방송 정보수정
     */
    @PostMapping("/edit")
    public String roomEdit(HttpServletRequest request){

        //TODO-방송 정보 조회 ? 서버? ...

        P_RoomEditVo apiData = new P_RoomEditVo();
        apiData.setMem_no(MemberVo.getUserInfo().getMem_no());
        apiData.setRoom_no(DalbitUtil.convertRequestParamToString(request, "s_room_no"));
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"));
        apiData.setTitle(DalbitUtil.convertRequestParamToString(request, "s_title"));
        apiData.setBackgroundImage(DalbitUtil.convertRequestParamToString(request, "s_bgImg"));
        apiData.setBackgroundImageGrade(DalbitUtil.convertRequestParamToInteger(request, "i_bgRacy"));
        apiData.setWelcomMsg(DalbitUtil.convertRequestParamToString(request, "s_welcome"));
        apiData.setEntry(DalbitUtil.convertRequestParamToInteger(request, "i_entry"));
        apiData.setAge(DalbitUtil.convertRequestParamToInteger(request, "i_age"));

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

        P_RoomListVo apiData = new P_RoomListVo();
        apiData.setMem_no(MemberVo.getUserInfo().getMem_no());
        apiData.setSubjectType(DalbitUtil.convertRequestParamToInteger(request, "i_type"));
        apiData.setPageNo(pageNo);
        apiData.setPageCnt(pageCnt);

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }



    /* ####################### 여기까지 API명세서 기준 작업완료 ######################## */




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


package com.demo.broadcast.controller;

import com.demo.broadcast.service.RoomService;
import com.demo.broadcast.vo.*;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.exception.GlobalException;
import com.demo.member.vo.MemberVo;
import com.demo.rest.service.RestService;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "방송방 생성")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "i_type",          value = "방송종류",                      required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "s_title",         value = "제목",                          required = true,   dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_bgImg",         value = "백그라운드 이미지 경로",        required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "i_bgRacy",        value = "백그라운드 선정성 등급",        required = false,  dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "s_welcome",       value = "환영 메시지",                   required = true,   dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_notice",        value = "공지사항",                      required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "i_entry",         value = "입장 (0:전체, 1:팬)",           required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "i_age",           value = "나이제한(0:전체, 1: 20세이상)", required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "i_os",            value = "OS 구분",                       required = true,   dataType = "int",    paramType = "query"),
    })
    @PostMapping("/create")
    public String roomCreate(HttpServletRequest request) throws GlobalException {
        //토큰생성
        HashMap map = new HashMap();
        String streamId = (String) restService.antCreate(StringUtil.convertRequestParamToString(request, "s_title")).get("streamId");
        String publishToken = (String) restService.antToken((String) map.get("streamId"), "publish").get("tokenId");
        //String playToken = (String) restService.antToken((String) map.get("streamId"), "play").get("tokenId");
        log.info("streamId: {}", streamId);
        log.info("publishToken: {}", publishToken);
        //log.info("playToken: {}", playToken);

        P_RoomCreateVo apiData = P_RoomCreateVo.builder()
            .mem_no(MemberVo.getUserInfo().getMem_no())
            .subjectType(StringUtil.convertRequestParamToInteger(request, "i_type"))
            .title(StringUtil.convertRequestParamToString(request, "s_title"))
            .backgroundImage(IMG_URL+"/"+StringUtil.convertRequestParamToString(request, "s_bgImg"))
            .backgroundImageGrade(StringUtil.convertRequestParamToInteger(request, "i_bgRacy"))
            .welcomMsg(StringUtil.convertRequestParamToString(request,"s_welcome"))
            .notice(StringUtil.convertRequestParamToString(request,"s_notice"))
            .entry(StringUtil.convertRequestParamToInteger(request,"i_entry"))
            .age(StringUtil.convertRequestParamToInteger(request,"i_age"))
            .os(StringUtil.convertRequestParamToInteger(request,"i_os"))
            .bj_streamid(streamId)
            .bj_publish_tokenid(publishToken)
            .build();

        String result = roomService.callBroadCastRoomCreate(apiData);

        return result;
    }

    /**
     * 방송방 참여하기
     */
    @ApiOperation(value = "방송방 참여하기")
    @PostMapping("/join")
    public String roomJoin(HttpServletRequest request) throws GlobalException{
        String roomNo = StringUtil.convertRequestParamToString(request, "s_room_no");

        //방참가를 위한 토큰 조회
        HashMap resultMap = roomService.callBroadCastRoomStreamIdRequest(roomNo);

        //Play 토큰 생성
        String bj_playToken = (String) restService.antToken((String) resultMap.get("bj_streamid"), "play").get("tokenId");
        String guest_playToken = (String) restService.antToken((String) resultMap.get("guest_streamid"), "play").get("tokenId");

        P_RoomJoinVo apiData = P_RoomJoinVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(roomNo)
                .guest_streamid(StringUtil.getStringMap(resultMap,"guest_streamid"))
                .guest_publish_tokenid(StringUtil.getStringMap(resultMap,"guest_publish_tokenid"))
                .guest_play_tokenid(guest_playToken)
                .bj_streamid(StringUtil.getStringMap(resultMap,"bj_streamid"))
                .bj_publish_tokenid(StringUtil.getStringMap(resultMap,"bj_publish_tokenid"))
                .bj_play_tokenid(bj_playToken)
                .build();

        String result = roomService.callBroadCastRoomJoin(apiData);

        return result;
    }

    /**
     * 방송방 나가기
     */
    @ApiOperation(value = "방송방 나가기")
    @PostMapping("/exit")
    public String roomExit(HttpServletRequest request){
        String roomNo = StringUtil.convertRequestParamToString(request, "s_room_no");
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
    @ApiOperation(value = "방송 정보수정")
    @PostMapping("/edit")
    public String roomEdit(HttpServletRequest request){

        String roomNo = StringUtil.convertRequestParamToString(request, "s_room_no");

        //TODO-방송 정보 조회 ? 서버? ...

        P_RoomEditVo apiData = P_RoomEditVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .room_no(roomNo)
                .build();

        String result = roomService.callBroadCastRoomEdit(apiData);

        return result;
    }

    /**
     * 방송방 리스트
     */
    @ApiOperation(value = "방송방 리스트")
    @GetMapping("/list")
    public String roomList(HttpServletRequest request){

        int pageNo = (StringUtil.convertRequestParamToInteger(request, "i_page")) == -1 ? 1 : StringUtil.convertRequestParamToInteger(request, "i_page");
        int pageCnt = (StringUtil.convertRequestParamToInteger(request, "i_records")) == -1 ? 5 : StringUtil.convertRequestParamToInteger(request, "i_records");

        P_RoomListVo apiData = P_RoomListVo.builder()
                .mem_no(MemberVo.getUserInfo().getMem_no())
                .subjectType(StringUtil.convertRequestParamToInteger(request, "i_type"))
                .pageNo(pageNo)
                .pageCnt(pageCnt)
                .build();

        String result = roomService.callBroadCastRoomList(apiData);

        return result;
    }

    /**
     * 방송방 참여자 리스트
     */
    @ApiOperation(value = "방송방 참여자 리스트")
    @GetMapping("/listeners")
    public String roomMemberList(){
        P_RoomMemberListVo apiSample = P_RoomMemberListVo.builder().build();
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomMemberList(apiSample);
        Status status;
        if(procedureOutputVo.getRet().equals(Status.방송참여자리스트_회원아님.getMessageCode())){
            status = Status.방송참여자리스트_회원아님;
        } else if(procedureOutputVo.getRet().equals(Status.방송참여자리스트없음.getMessageCode())){
            status = Status.방송참여자리스트없음;
        } else {
            status = Status.방송참여자리스트_조회;
        }
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(status, procedureOutputVo.getOutputBox())));
    }



    /**
     * 방송종료
     */
    @ApiOperation(value = "방송종료")
    @DeleteMapping("/{brodNo}")
    public String deleteBrod(@PathVariable String brodNo ){

        HashMap data = new HashMap();

        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송종료, data)));
    }


    /**
     * 방송 정보조회
     */
    @ApiOperation(value = "방송 정보조회")
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


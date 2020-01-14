package com.demo.broadcast.controller;

import com.demo.broadcast.service.RoomService;
import com.demo.broadcast.vo.*;
import com.demo.common.code.Status;
import com.demo.common.vo.JsonOutputVo;
import com.demo.common.vo.ProcedureOutputVo;
import com.demo.common.vo.ProcedureVo;
import com.demo.exception.GlobalException;
import com.demo.member.vo.MemberVo;
import com.demo.rest.service.RestService;
import com.demo.util.CommonUtil;
import com.demo.util.GsonUtil;
import com.demo.util.MessageUtil;
import com.demo.util.StringUtil;
import com.google.gson.Gson;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Value;


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
            @ApiImplicitParam(name = "i_bgRacy",        value = "백그라운드 선정성 등급",        required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_welcome",       value = "환영 메시지",                   required = true,   dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_notice",        value = "공지사항",                      required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "i_entry",         value = "입장 (0:전체, 1:팬)",           required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "i_age",           value = "나이제한(0:전체, 1: 20세이상)", required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "i_os",            value = "OS 구분",                       required = true,   dataType = "int",    paramType = "query"),
            @ApiImplicitParam(name = "s_deviceId",      value = "디바이스 고유아이디",           required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_deviceToken",   value = "디바이스 토큰",                 required = false,  dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "s_appVer",        value = "앱 버전",                       required = false,  dataType = "string", paramType = "query")
    })
    @PostMapping("/create")
    public String createBrod(HttpServletRequest request) throws GlobalException {
        //토큰생성
        HashMap map = new HashMap();
        String streamId = (String) restService.antCreate(request.getParameter("s_title")).get("streamId");
        String publishToken = (String) restService.antToken((String) map.get("streamId"), "publish").get("tokenId");
        String playToken = (String) restService.antToken((String) map.get("streamId"), "play").get("tokenId");
        log.info("streamId: {}", streamId);
        log.info("publishToken: {}", publishToken);
        log.info("playToken: {}", playToken);

        P_RoomCreateVo apiData = P_RoomCreateVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .subjectType(StringUtils.defaultIfEmpty(request.getParameter("i_type"), "").trim())
                .title(StringUtils.defaultIfEmpty(request.getParameter("s_title"), "").trim())
                .backgroundImage(IMG_URL+StringUtils.defaultIfEmpty(request.getParameter("s_bgImg"), "").trim())
                .backgroundImageGrade(StringUtils.defaultIfEmpty(request.getParameter("i_bgRacy"), "").trim())
                .welcomMsg(StringUtils.defaultIfEmpty(request.getParameter("s_welcome"), "").trim())
                .notice(StringUtils.defaultIfEmpty(request.getParameter("s_notice"), "").trim())
                .entry(StringUtils.defaultIfEmpty(request.getParameter("i_entry"), "").trim())
                .age(StringUtils.defaultIfEmpty(request.getParameter("i_age"), "").trim())
                .os(StringUtils.defaultIfEmpty(request.getParameter("i_os"), "").trim())
                .deviceUuid(StringUtils.defaultIfEmpty(request.getParameter("s_deviceId"), "").trim())
                .deviceToken(StringUtils.defaultIfEmpty(request.getParameter("s_deviceToken"), "").trim())
                .appVersion(StringUtils.defaultIfEmpty(request.getParameter("s_appVer"), "").trim())
                .bj_streamid(streamId)
                .bj_publish_tokenid(publishToken)
                .bj_play_tokenid(playToken)
                .build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomCreate(apiData);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String roomNo = CommonUtil.isEmpty(resultMap) ? null : (String) resultMap.get("room_no");
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);
        log.info("방번호 추출: {}", roomNo);
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("room_no", StringUtil.isNullToString(roomNo));
        returnMap.put("bj_streamid",streamId);
        returnMap.put("bj_publish_tokenid", publishToken);
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송생성.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송생성_회원아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송중인방존재.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송중인방존재, procedureVo.getData())));
        } else {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방생성실패)));
        }

        return result;
    }

    /**
     * 방송방 참여하기
     */
    @ApiOperation(value = "방송방 참여하기")
    @PostMapping("/{roomNo}/join")
    public String inBrod(@PathVariable String roomNo){

        //참가를 위한 토큰 받기
        HashMap resultMap = callBroadCastRoomStreamIdRequest(roomNo);

        P_RoomJoinVo apiData = P_RoomJoinVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .room_no(roomNo)
                .guest_streamid((String) resultMap.get("guest_streamid"))
                .guest_publish_tokenid((String) resultMap.get("guest_publish_tokenid"))
                .guest_play_tokenid((String) resultMap.get("guest_play_tokenid"))
                .bj_streamid((String) resultMap.get("bj_streamid"))
                .bj_publish_tokenid((String) resultMap.get("bj_publish_tokenid"))
                .bj_play_tokenid((String) resultMap.get("bj_play_tokenid"))
                .build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomJoin(apiData);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        HashMap returnMap = new HashMap();
        returnMap.put("mem_no",MemberVo.getUserInfo().getMemNo());
        returnMap.put("room_no",roomNo);
        returnMap.put("guest_streamid",apiData.getGuest_streamid());
        returnMap.put("guest_publish_tokenid",apiData.getGuest_publish_tokenid());
        returnMap.put("guest_play_tokenid",apiData.getGuest_play_tokenid());
        returnMap.put("bj_streamid",apiData.getBj_streamid());
        returnMap.put("bj_publish_tokenid",apiData.getBj_publish_tokenid());
        returnMap.put("bj_play_tokenid",apiData.getBj_play_tokenid());
        log.info("returnMap: {}",returnMap);
        procedureVo.setData(returnMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송참여성공.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여성공, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_회원아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_해당방이없음, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_종료된방송, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_이미참가, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여_입장제한.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여_입장제한, procedureVo.getData())));
        } else {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
        }

        return result;
    }

    /**
     * 방송방 나가기
     */
    @ApiOperation(value = "방송방 나가기")
    @PostMapping("/{roomNo}/exit")
    public String outBrod(@PathVariable String roomNo){

        P_RoomExitVo apiData = P_RoomExitVo.builder()
                .mem_no(MemberVo.getUserInfo().getMemNo())
                .room_no(roomNo)
                .build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomExit(apiData);

        String result;
        if(procedureVo.getRet().equals(Status.방송나가기.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송나가기_회원아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_회원아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송나가기_해당방이없음.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_해당방이없음, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송나가기_종료된방송.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_종료된방송, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송나가기_방참가자아님.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기_방참가자아님, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송나가기실패.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송나가기실패, procedureVo.getData())));
        } else {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방참가실패)));
        }

        return result;
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

    /**
     * 방송 정보수정
     */
    @ApiOperation(value = "방송 정보수정")
    @PostMapping("/{brodNo}")
    public String updateBrod(@PathVariable String brodNo){

        HashMap data = new HashMap();
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.수정, data)));
    }

    /**
     * 방송방 리스트
     */
    @ApiOperation(value = "방송방 리스트")
    @PostMapping("/brodList")
    public String selectBrodList(){
        P_RoomListVo apiSample = P_RoomListVo.builder().build();
        ProcedureOutputVo procedureOutputVo = roomService.callBroadCastRoomList(apiSample);
        Status status;
        if(procedureOutputVo.getRet().equals(Status.방송리스트_회원아님.getMessageCode())){
            status = Status.방송리스트_회원아님;
        } else if(procedureOutputVo.getRet().equals(Status.방송리스트없음.getMessageCode())){
            status = Status.방송리스트없음;
        } else {
            status = Status.방송리스트_조회;
        }
        return new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(status, procedureOutputVo.getOutputBox())));
    }

    /**
     * 방송방 참여자 리스트
     */
    @ApiOperation(value = "방송방 참여자 리스트")
    @PostMapping("/brodMemberList")
    public String selectBrodMemberList(){
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
     * 방송방 참가를 위해 스트림아이디 토큰아이디 받아오기
     */
    public HashMap callBroadCastRoomStreamIdRequest(String roomNo){
        // 방송방 참가를 위해 스트림아이디 토큰아이디 받아오기

        P_RoomJoinTokenVo apiData = P_RoomJoinTokenVo.builder()
                .room_no(roomNo)
                .build();
        ProcedureVo procedureVo = roomService.callBroadCastRoomStreamIdRequest(apiData);
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", resultMap);

        String result;
        if(procedureVo.getRet().equals(Status.방송참여토큰발급.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여토큰발급, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_해당방이없음.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여토큰_해당방이없음, procedureVo.getData())));
        } else if (procedureVo.getRet().equals(Status.방송참여토큰_방장이없음.getMessageCode())) {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여토큰_방장이없음, procedureVo.getData())));
        } else {
            result = new Gson().toJson(messageUtil.setJsonOutputVo(new JsonOutputVo(Status.방송참여토큰발급_실패)));
        }

        log.debug("방송방 참여 토큰발급 결과: {}", result);

        return resultMap;
    }
}


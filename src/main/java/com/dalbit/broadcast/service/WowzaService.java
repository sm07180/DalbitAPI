package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.RoomInfoVo;
import com.dalbit.broadcast.vo.RoomMemberInfoVo;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.RoomCreateVo;
import com.dalbit.broadcast.vo.request.RoomInfo;
import com.dalbit.broadcast.vo.request.RoomJoinVo;
import com.dalbit.broadcast.vo.request.RoomTokenVo;
import com.dalbit.common.code.Code;
import com.dalbit.common.code.Status;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class WowzaService {
    @Autowired
    RoomDao roomDao;
    @Autowired
    SocketService socketService;
    @Autowired
    CommonService commonService;
    @Autowired
    RestService restService;
    @Autowired
    CommonDao commonDao;
    @Autowired
    RoomService roomService;

    @Value("${wowza.prefix}")
    String WOWZA_PREFIX;
    @Value("${wowza.real.server}")
    String[] WOWZA_REALSERVER;

    public HashMap doUpdateWowzaState(HttpServletRequest request){
        HashMap result = new HashMap();
        String streamName = request.getParameter("streamName");
        String action = request.getParameter("action");
        if(DalbitUtil.isEmpty(streamName) || DalbitUtil.isEmpty(action) || !("liveStreamStarted".equals(action) || "liveStreamEnded".equals(action))){
            result.put("status", Status.파라미터오류);
        }else{
            String roomNo = streamName.toLowerCase().substring(WOWZA_PREFIX.toLowerCase().length());
            String guestNo = "";
            boolean isGuest = false;
            if(streamName.indexOf("_") > -1){
                roomNo = StringUtils.split(streamName, "_")[0].toLowerCase().substring(WOWZA_PREFIX.toLowerCase().length());
                guestNo = StringUtils.split(streamName, "_")[1];
                isGuest = true;
            }

            RoomOutVo target = getRoomInfo(roomNo);
            boolean roomCheck = false;
            if(target != null && target.getState() != 4) {
                if(isGuest == false){
                    int old_state = target.getState();
                    int new_state = -1;
                    SocketVo vo = socketService.getSocketVo(target.getRoomNo(), target.getBjMemNo(), DalbitUtil.isLogin(request));
                    if("liveStreamStarted".equals(action)){
                        new_state = 1;
                    }else{
                        new_state = 0;
                    }
                    if(old_state != new_state){
                        P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
                        pRoomStateUpdateVo.setMem_no(target.getBjMemNo());
                        pRoomStateUpdateVo.setRoom_no(target.getRoomNo());
                        pRoomStateUpdateVo.setState(new_state);
                        ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
                        roomDao.callBroadCastRoomStateUpate(procedureVo);
                    }
                    //소켓발송
                    try{
                        socketService.changeRoomState(target.getRoomNo(), target.getBjMemNo(), old_state, new_state, target.getBjMemNo(), true, vo, "liveStreamStarted".equals(action) ? "1" : "0");
                        vo.resetData();
                    }catch(Exception e){
                        log.info("Socket Service changeRoomState Exception {}", e);
                    }
                    roomCheck = true;
                    result.put("status", Status.방송방상태변경_성공);
                } else {
                    //TODO: 게스트 활동 확인
                }

                //생성된 방송 WEBRTC EDGE 생성 호출
                if(roomCheck){
                    for(String server : WOWZA_REALSERVER) {
                        try {
                            sendFirstEdge(server, streamName);
                        } catch (Exception e) {
                            if("local".equals(DalbitUtil.getActiveProfile())){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }else{
                result.put("status", Status.방정보보기_실패);
            }
        }
        return result;
    }

    public HashMap doCreateBroadcast(RoomCreateVo roomCreateVo, HttpServletRequest request) throws GlobalException {
        HashMap result = new HashMap();
        var codeVo = commonService.selectCodeDefine(new CodeVo(Code.시스템설정_방송방막기.getCode(), Code.시스템설정_방송방막기.getDesc()));
        if(!DalbitUtil.isEmpty(codeVo)){
            if(codeVo.getValue().equals("Y")){
                result.put("status", Status.설정_방생성_참여불가상태);
                return result;
            }
        }

        // 부적절한문자열 체크 ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(roomCreateVo.getTitle())){
            result.put("status", Status.부적절한문자열);
            return result;
        }

        String systemBanWord = commonService.banWordSelect();
        //금지어 체크(제목)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, roomCreateVo.getTitle())){
            result.put("status", Status.방송방생성제목금지);
            return result;
        }

        //금지어 체크(인사말)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, roomCreateVo.getWelcomMsg())){
            result.put("status", Status.방송방생성인사말금지);
            return result;
        }

        P_RoomCreateVo pRoomCreateVo = new P_RoomCreateVo();
        pRoomCreateVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomCreateVo.setSubjectType(roomCreateVo.getRoomType());
        pRoomCreateVo.setTitle(roomCreateVo.getTitle());
        pRoomCreateVo.setBackgroundImage(roomCreateVo.getBgImg());
        pRoomCreateVo.setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomCreateVo.getBgImgRacy()));
        pRoomCreateVo.setWelcomMsg(roomCreateVo.getWelcomMsg());
        pRoomCreateVo.setNotice(roomCreateVo.getNotice());
        pRoomCreateVo.setEntryType(roomCreateVo.getEntryType());

        DeviceVo deviceVo = new DeviceVo(request);
        pRoomCreateVo.setOs(deviceVo.getOs());
        pRoomCreateVo.setDeviceUuid(deviceVo.getDeviceUuid());
        pRoomCreateVo.setDeviceToken(deviceVo.getDeviceToken());
        pRoomCreateVo.setAppVersion(deviceVo.getAppVersion());
        pRoomCreateVo.setIsWowza(1);

        String bgImg = pRoomCreateVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            int random = Integer.parseInt(DalbitUtil.randomBgValue());
            bgImg = Code.포토_배경_디폴트_PREFIX.getCode()+"/"+Code.배경이미지_파일명_PREFIX.getCode()+"200708_"+random+".jpg";
        }else{
            if(bgImg.startsWith(Code.포토_배경_임시_PREFIX.getCode())){
                isDone = true;
            }
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        pRoomCreateVo.setBackgroundImage(bgImg);

        ProcedureVo procedureVo = new ProcedureVo(pRoomCreateVo);
        roomDao.callBroadCastRoomCreate(procedureVo);

        if(procedureVo.getRet().equals(Status.방송생성.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.isNullToString(resultMap.get("room_no"));
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomCreateVo.getBackgroundImage()), request);
            }
            RoomOutVo target = getRoomInfo(roomNo);
            RoomMemberInfoVo memberInfoVo = new RoomMemberInfoVo();
            memberInfoVo.setRank(DalbitUtil.getIntMap(resultMap, "rank"));
            memberInfoVo.setAuth(3);
            memberInfoVo.setCtrlRole("1111111111");
            memberInfoVo.isFan(false);
            memberInfoVo.setHashStory(false);
            memberInfoVo.isLike(false);
            memberInfoVo.setLikes(DalbitUtil.getIntMap(resultMap, "good"));
            memberInfoVo.setRemainTime(DalbitUtil.getLongMap(resultMap, "remainTime"));
            memberInfoVo.setUseBoost(DalbitUtil.getIntMap(resultMap, "booster") > 0);
            HashMap fanRankMap = commonService.getKingFanRankList(roomNo);
            memberInfoVo.setFanRank((List)fanRankMap.get("list"));
            try{
                memberInfoVo.setKingMember((String)fanRankMap.get("kingMemNo"), (String)fanRankMap.get("kingNickNm"), (ImageVo) fanRankMap.get("kingProfImg"));
            }catch(Exception e) {}
            HashMap fanBadgeMap = new HashMap();
            fanBadgeMap.put("mem_no", target.getBjMemNo());
            fanBadgeMap.put("type", 3);
            List fanBadgeList = commonDao.callMemberBadgeSelect(fanBadgeMap);
            if(!DalbitUtil.isEmpty(fanBadgeList)){
                memberInfoVo.setFanBadgeList(fanBadgeList);
            }

            result.put("status", Status.방송생성);
            result.put("data", new RoomInfoVo(target, memberInfoVo, WOWZA_PREFIX));
        } else if (procedureVo.getRet().equals(Status.방송생성_회원아님.getMessageCode())) {
            result.put("status", Status.방송생성_회원아님);
        } else if (procedureVo.getRet().equals(Status.방송중인방존재.getMessageCode())) {
            result.put("status", Status.방송중인방존재);
        } else if (procedureVo.getRet().equals(Status.방송생성_deviceUuid비정상.getMessageCode())) {
            result.put("status", Status.방송생성_deviceUuid비정상);
        } else {
            result.put("status", Status.방생성실패);
        }
        return result;
    }

    public HashMap doJoinBroadcast(RoomJoinVo roomJoinVo, HttpServletRequest request) throws GlobalException{

        HashMap result = new HashMap();
        //비회원 참여 불가
        if(!DalbitUtil.isLogin(request)){
            result.put("status", Status.로그인필요);
            return result;
        }

        //방 생성 접속 불가 상태 체크
        var codeVo = commonService.selectCodeDefine(new CodeVo(Code.시스템설정_방송방막기.getCode(), Code.시스템설정_방송방막기.getDesc()));
        if(!DalbitUtil.isEmpty(codeVo)){
            if(codeVo.getValue().equals("Y")){
                result.put("status", Status.설정_방생성_참여불가상태);
                return result;
            }
        }

        P_RoomJoinVo pRoomJoinVo = new P_RoomJoinVo();
        pRoomJoinVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        pRoomJoinVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomJoinVo.setRoom_no(roomJoinVo.getRoomNo());
        if(roomJoinVo.getShadow() != null && roomJoinVo.getShadow() == 1){
            pRoomJoinVo.setShadow(1);
        }else{
            pRoomJoinVo.setShadow(0);
        }
        DeviceVo deviceVo = new DeviceVo(request);
        pRoomJoinVo.setOs(deviceVo.getOs());
        pRoomJoinVo.setDeviceUuid(deviceVo.getDeviceUuid());
        pRoomJoinVo.setIp(deviceVo.getIp());
        pRoomJoinVo.setAppVersion(deviceVo.getAppVersion());
        pRoomJoinVo.setDeviceToken(deviceVo.getDeviceToken());
        pRoomJoinVo.setIsHybrid(deviceVo.getIsHybrid());

        ProcedureVo procedureVo = new ProcedureVo(pRoomJoinVo);
        roomDao.callBroadCastRoomJoin(procedureVo);

        if(procedureVo.getRet().equals(Status.방송참여성공.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            RoomOutVo target = getRoomInfo(pRoomJoinVo.getRoom_no(), pRoomJoinVo.getMem_no(), pRoomJoinVo.getMemLogin());
            RoomInfoVo roomInfoVo = getRoomInfo(target, resultMap, request);
            SocketVo vo = socketService.getSocketVo(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            if(target.getState() == 2 || target.getState() == 3 || target.getState() == 0){
                try{
                    socketService.changeRoomState(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), target.getState(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), "join", vo);
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service changeRoomState Exception {}", e);
                }
            }

            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", roomInfoVo.getLikes());
                socketMap.put("rank", roomInfoVo.getRank());
                socketMap.put("fanRank", roomInfoVo.getFanRank());
                socketService.changeCount(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            //애드브릭스 전달을 위한 데이터 생성
            //adbrixService("roomJoin", "1151231231312")

            result.put("status", Status.방송참여성공);
            result.put("data", roomInfoVo);
        } else if (procedureVo.getRet().equals(Status.방송참여_회원아님.getMessageCode())) {
            result.put("status", Status.방송참여_회원아님);
        } else if (procedureVo.getRet().equals(Status.방송참여_해당방이없음.getMessageCode())) {
            result.put("status", Status.방송참여_해당방이없음);
        } else if (procedureVo.getRet().equals(Status.방송참여_종료된방송.getMessageCode())) {
            result.put("status", Status.방송참여_종료된방송);
        } else if (procedureVo.getRet().equals(Status.방송참여_이미참가.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String auth = DalbitUtil.getStringMap(resultMap, "auth");
            CodeVo codeVo1 = commonService.getCodeList("roomRight").stream().filter(code -> code.getCdNm().equals("방장")).findFirst().orElse(null);
            if((!DalbitUtil.isEmpty(codeVo1) && auth.equals(codeVo1.getCd()))){ //방송중 다른방 참가
                result.put("status", Status.방송참여_방송중);
            }else{
                String deviceUuid = DalbitUtil.getStringMap(resultMap, "deviceUuid");
                deviceUuid = deviceUuid == null ? "" : deviceUuid.trim();
                if(deviceUuid.equals(pRoomJoinVo.getDeviceUuid())){ //동일기기 참가일때 /reToken과 동일로직
                    RoomTokenVo roomTokenVo = new RoomTokenVo();
                    roomTokenVo.setRoomNo(roomJoinVo.getRoomNo());
                    result = getBroadcast(roomTokenVo, request);
                }else{
                    result.put("status", Status.방송참여_이미참가);
                }
            }
        } else if (procedureVo.getRet().equals(Status.방송참여_입장제한.getMessageCode())) {
            result.put("status", Status.방송참여_입장제한);
        } else if (procedureVo.getRet().equals(Status.방송참여_나이제한.getMessageCode())) {
            result.put("status", Status.방송참여_나이제한);
        } else if (procedureVo.getRet().equals(Status.방송참여_강퇴시간제한.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            result.put("status", Status.방송참여_강퇴시간제한);
            result.put("data", data);
        } else if (procedureVo.getRet().equals(Status.방송참여_블랙리스트.getMessageCode())) {
            result.put("status", Status.방송참여_블랙리스트);
        } else if (procedureVo.getRet().equals(Status.방송참여_다른기기.getMessageCode())) {
            result.put("status", Status.방송참여_다른기기);
        } else if (procedureVo.getRet().equals(Status.방송참여_비회원IP중복.getMessageCode())) {
            result.put("status", Status.방송참여_비회원IP중복);
        } else {
            result.put("status", Status.방참가실패);
        }
        return result;
    }

    public HashMap getBroadcast(RoomTokenVo roomTokenVo, HttpServletRequest request) throws GlobalException{
        HashMap result = new HashMap();
        RoomOutVo target = getRoomInfo(roomTokenVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
        if(target != null){
            if(target.getState() == 4){
                result.put("status", Status.방정보보기_해당방없음);
            }else{
                P_RoomStreamTokenVo pRoomStreamTokenVo = new P_RoomStreamTokenVo();
                pRoomStreamTokenVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
                pRoomStreamTokenVo.setMem_no(MemberVo.getMyMemNo(request));
                pRoomStreamTokenVo.setRoom_no(roomTokenVo.getRoomNo());
                pRoomStreamTokenVo.setBj_streamid(WOWZA_PREFIX + roomTokenVo.getRoomNo());
                ProcedureVo procedureUpdateVo = new ProcedureVo(pRoomStreamTokenVo);
                roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);
                if(Status.스트림아이디_조회성공.getMessageCode().equals(procedureUpdateVo.getRet())) {
                    HashMap resultUpdateMap = new Gson().fromJson(procedureUpdateVo.getExt(), HashMap.class);
                    RoomInfoVo roomInfoVo = getRoomInfo(target, resultUpdateMap, request);

                    result.put("status", Status.방정보보기);
                    result.put("data", roomInfoVo);
                }else if(Status.스트림아이디_회원아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", Status.스트림아이디_회원아님);
                }else if(Status.스트림아이디_해당방없음.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", Status.스트림아이디_해당방없음);
                }else if(Status.스트림아이디_요청회원_방소속아님.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", Status.스트림아이디_요청회원_방소속아님);
                }else{
                    result.put("status", Status.방정보보기_실패);
                }
            }
        }else{
            result.put("status", Status.방정보보기_실패);
        }
        return result;
    }

    public RoomOutVo getRoomInfo(String roomNo){
        return getRoomInfo(roomNo, "0", 99);
    }

    public RoomOutVo getRoomInfo(String roomNo, String memNo, boolean isLogin) {
        return getRoomInfo(roomNo, memNo, (isLogin ? 1 : 0));
    }

    public RoomOutVo getRoomInfo(String roomNo, String memNo, int isLogin) {
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(isLogin);
        pRoomInfoViewVo.setMem_no(memNo);
        pRoomInfoViewVo.setRoom_no(roomNo);
        ProcedureVo procedureInfoViewVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureInfoViewVo);
        if (procedureInfoViewVo.getRet().equals(Status.방정보보기.getMessageCode())){
            roomInfoViewVo.setExt(procedureInfoViewVo.getExt());
            ProcedureOutputVo procedureRoomInfoOutputVo = new ProcedureOutputVo(procedureInfoViewVo, new RoomOutVo(roomInfoViewVo));
            return (RoomOutVo) procedureRoomInfoOutputVo.getOutputBox();
        }
        return null;
    }

    public RoomInfoVo getRoomInfo(RoomOutVo target, HttpServletRequest request){
        return getRoomInfo(target, null, request);
    }
    public RoomInfoVo getRoomInfo(RoomOutVo target, HashMap resultMap, HttpServletRequest request){
        RoomMemberInfoVo memberInfoVo = new RoomMemberInfoVo();
        if(!DalbitUtil.isEmpty(resultMap)){
            memberInfoVo.setRemainTime(DalbitUtil.getLongMap(resultMap, "remainTime"));
            memberInfoVo.setLikes(DalbitUtil.getIntMap(resultMap, "good"));
            memberInfoVo.setRank(DalbitUtil.getIntMap(resultMap, "rank"));
            memberInfoVo.setAuth(DalbitUtil.getIntMap(resultMap, "auth"));
            memberInfoVo.setCtrlRole(DalbitUtil.getStringMap(resultMap, "controlRole"));
            memberInfoVo.isFan("1".equals(DalbitUtil.getStringMap(resultMap, "isFan")));
            memberInfoVo.isLike(DalbitUtil.isLogin(request) ? "1".equals(DalbitUtil.getStringMap(resultMap, "isGood")) : true);
        }
        memberInfoVo.setHashStory(false);
        memberInfoVo.setUseBoost(roomService.existsBoostByRoom(target.getRoomNo(), MemberVo.getMyMemNo(request)));
        HashMap fanRankMap = commonService.getKingFanRankList(target.getRoomNo());
        memberInfoVo.setFanRank((List)fanRankMap.get("list"));
        try{
            memberInfoVo.setKingMember((String)fanRankMap.get("kingMemNo"), (String)fanRankMap.get("kingNickNm"), (ImageVo) fanRankMap.get("kingProfImg"));
        }catch(Exception e) {}
        HashMap fanBadgeMap = new HashMap();
        fanBadgeMap.put("mem_no", target.getBjMemNo());
        fanBadgeMap.put("type", 3);
        List fanBadgeList = commonDao.callMemberBadgeSelect(fanBadgeMap);
        if(!DalbitUtil.isEmpty(fanBadgeList)){
            memberInfoVo.setFanBadgeList(fanBadgeList);
        }

        return new RoomInfoVo(target, memberInfoVo, WOWZA_PREFIX);
    }

    @Async("threadTaskExecutor")
    public void sendFirstEdge(String svrDomain, String streamName) throws Exception{
        WowzaWebSocketClient  client = new WowzaWebSocketClient(new URI("wss://" + svrDomain + "/" + DalbitUtil.getProperty("wowza.wss.url.endpoint")));
        client.addMessageHandler(new WowzaWebSocketClient.MessageHandler() {
            @Override
            public void handleMessage(String message) {
                /*log.debug("receive WOWZA message : " + message);
                HashMap result = new Gson().fromJson(message, HashMap.class);
                if(result != null && result.containsKey("status") && "200".equals(((Double)result.get("status")).toString())) {
                    //if("getOffer".equals((String))){

                    //}
                }else if(result != null && result.containsKey("status") && "504".equals(((Double)result.get("status")).toString())){
                }else{
                }*/
                client.close();
            }
        });

        HashMap message = new HashMap();
        HashMap streamInfo = new HashMap();
        streamInfo.put("applicationName", "edge");
        streamInfo.put("sessionId", "");
        streamInfo.put("streamName", streamName + "_opus");
        message.put("command", "getOffer");
        message.put("direction", "play");
        message.put("streamInfo", streamInfo);
        String sendMessage = new Gson().toJson(message);
        client.sendMessage(sendMessage);
        log.debug("WOWZA SEND MESSAGE : " + sendMessage);
    }
}

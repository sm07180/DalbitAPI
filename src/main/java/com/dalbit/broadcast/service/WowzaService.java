package com.dalbit.broadcast.service;

import com.dalbit.agora.media.RtcTokenBuilder;
import com.dalbit.broadcast.dao.GuestDao;
import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.proc.Broadcast;
import com.dalbit.broadcast.vo.*;
import com.dalbit.broadcast.vo.RoomMemberInfoVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.*;
import com.dalbit.common.code.*;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.BadgeService;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.event.service.DallagersEventService;
import com.dalbit.event.service.EventService;
import com.dalbit.event.service.MoonLandService;
import com.dalbit.event.vo.MoonLandInfoVO;
import com.dalbit.event.vo.procedure.P_AttendanceCheckVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MemberDao;
import com.dalbit.member.service.MypageService;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.*;
import com.dalbit.member.vo.request.BroadcastOptionAddVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.dalbit.util.IPUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.dalbit.agora.media.RtcTokenBuilder.Role;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    MemberDao memberDao;
    @Autowired
    RoomService roomService;
    @Autowired
    GuestDao guestDao;
    @Autowired
    MypageService mypageService;
    @Autowired
    EventService eventService;
    @Autowired
    IPUtil ipUtil;
    @Autowired
    BadgeService badgeService;
    @Autowired
    ActionService actionService;
    @Autowired
    WowzaSocketService wowzaSocketService;
    @Autowired
    MiniGameService miniGameService;

    @Autowired TTSService ttsService;
    @Autowired MoonLandService moonLandService;
    @Autowired VoteService voteService;
    @Autowired DallagersEventService dallagersEventService;
    @Autowired Broadcast broadcast;

    @Value("${wowza.prefix}")
    String WOWZA_PREFIX;
    @Value("${wowza.audio.server}")
    String[] WOWZA_AUDIO_SERVER;
    @Value("${wowza.video.server}")
    String[] WOWZA_VIDEO_SERVER;
    @Value("${wowza.aac}")
    String WOWZA_ACC;
    @Value("${wowza.opus}")
    String WOWZA_OPUS;

    @Value("${agora.app.id}")
    String AGORA_APP_ID;
    @Value("${agora.app.cert}")
    String AGORA_APP_CERT;
    @Value("${agora.app.area}")
    String[] AGORA_APP_AREA;
    @Value("${agora.app.minbit}")
    int AGORA_APP_MINBIT;
    @Value("${agora.app.maxbit}")
    int AGORA_APP_MAXBIT;
    @Value("${agora.app.ligh}")
    double AGORA_APP_LIGH;
    @Value("${agora.app.smoot}")
    double AGORA_APP_SMOOT;
    @Value("${agora.app.redne}")
    double AGORA_APP_REDNE;
    @Value("${agora.app.shar}")
    double AGORA_APP_SHAR;

    @Autowired
    GsonUtil gsonUtil;

    static int expirationTimeInSeconds = 10800;


    public HashMap doUpdateWowzaState(HttpServletRequest request){
        StringBuffer body = new StringBuffer();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(request.getInputStream()));
            String buffer;
            while ((buffer = input.readLine()) != null){
                body.append(buffer);
            }
        }catch(IOException e){}

        HashMap result = new HashMap();
        String streamName = "";
        String action = "";
        if(body.length() > 0){
            try {
                HashMap bodyMap = new Gson().fromJson(body.toString(), HashMap.class);
                streamName = DalbitUtil.getStringMap(bodyMap, "streamName");
                action = DalbitUtil.getStringMap(bodyMap, "action");
            }catch(Exception e){}
        }
        if(DalbitUtil.isEmpty(streamName) || DalbitUtil.isEmpty(action) || !("liveStreamStarted".equals(action) || "liveStreamEnded".equals(action))){
            result.put("status", CommonStatus.??????????????????);
        }else {
            log.debug("call wowza hook {}, {}", streamName, action);
            if (streamName.toLowerCase().endsWith(WOWZA_ACC) || streamName.toLowerCase().endsWith(WOWZA_OPUS)) {
                streamName = StringUtils.replace(streamName.toLowerCase(), WOWZA_OPUS, "");
                streamName = StringUtils.replace(streamName.toLowerCase(), WOWZA_ACC, "");
            }
            String roomNo = streamName.toLowerCase().substring(WOWZA_PREFIX.toLowerCase().length());
            String guestNo = "";
            boolean isGuest = false;

            if(roomNo.indexOf("_") > 1){
                String[] tmp = StringUtils.split(roomNo, "_");
                guestNo = tmp[1];
                roomNo = tmp[0];
                isGuest = true;
            }else if(roomNo.length() > 14){
                guestNo = roomNo.substring(14);
                roomNo = roomNo.substring(0, 14);
                isGuest = true;
            }
            log.debug("call wowza hook roomNo : {}, guestNo : {}", roomNo, guestNo);

            RoomOutVo target = getRoomInfo(roomNo, request);
            //boolean roomCheck = false;
            if(target != null && target.getState() != 4) {
                /*if("v".equals(target.getMediaType())) {
                    wowzaSocketService.wowzaDisconnect(target.getMediaType(), streamName, null);
                    log.debug("======================== call video edge disconnect");
                }*/

                if(isGuest == true && !DalbitUtil.isEmpty(guestNo) && guestNo.equals(target.getBjMemNo())){
                    isGuest = false;
                    guestNo = "";
                }
                if(isGuest == false){
                    int old_state = target.getState();
                    if(old_state == 6 && "liveStreamStarted".equals(action)){
                        P_RoomStateUpdateVo pRoomStateUpdateVo = new P_RoomStateUpdateVo();
                        pRoomStateUpdateVo.setMem_no(target.getBjMemNo());
                        pRoomStateUpdateVo.setRoom_no(target.getRoomNo());
                        pRoomStateUpdateVo.setState(1);
                        ProcedureVo procedureVo = new ProcedureVo(pRoomStateUpdateVo);
                        roomDao.callBroadCastRoomStateUpate(procedureVo);
                    }

                    //roomCheck = true;
                    result.put("status", BroadcastStatus.?????????????????????_??????);
                } else {
                    //TODO ?????? ??????????????? ???????????? ??????????????? ??????
                    HashMap params = new HashMap();
                    params.put("roomNo", roomNo);
                    params.put("memNo", guestNo);
                    HashMap guestInfo = guestDao.selectGuestInfo(params);
                    if(!DalbitUtil.isEmpty(guestInfo)){
                        /*SocketVo vo = socketService.getSocketVo(target.getRoomNo(), guestNo, true);
                        GuestInfoVo guestInfoVo = new GuestInfoVo();
                        guestInfoVo.setMode(8);
                        guestInfoVo.setMemNo(guestNo);
                        guestInfoVo.setNickNm((String) guestInfo.get("mem_nick"));
                        guestInfoVo.setProfImg(new ImageVo(guestInfo.get("image_profile"), (String)guestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));
                        String gstRtmpOrigin;
                        String gstRtmpEdge;
                        String gstWebRtcUrl;
                        if("a".equals(target.getMediaType())){
                            gstRtmpOrigin = DalbitUtil.getProperty("wowza.audio.rtmp.origin") + "/" + streamName;// + "_aac";
                            gstRtmpEdge =  DalbitUtil.getProperty("wowza.audio.rtmp.edge") + "/" + streamName + "_aac";
                            gstWebRtcUrl = DalbitUtil.getProperty("wowza.audio.wss.url") ;
                        }else{
                            gstRtmpOrigin = DalbitUtil.getProperty("wowza.video.rtmp.origin") + "/" + streamName;// + "_aac";
                            gstRtmpEdge =  DalbitUtil.getProperty("wowza.video.rtmp.edge") + "/" + streamName + "_aac";
                            gstWebRtcUrl = guestNo.equals(MemberVo.getMyMemNo(request)) ? DalbitUtil.getProperty("wowza.video.wss.url.origin") : DalbitUtil.getProperty("wowza.video.wss.url.edge");
                        }
                        String gstWebRtcAppName = "edge";
                        String gstWebRtcStreamName = streamName + "_opus";
                        guestInfoVo.setRtmpOrigin(gstRtmpOrigin);
                        guestInfoVo.setRtmpEdge(gstRtmpEdge);
                        guestInfoVo.setWebRtcUrl(gstWebRtcUrl);
                        guestInfoVo.setWebRtcAppName(gstWebRtcAppName);
                        guestInfoVo.setWebRtcStreamName(gstWebRtcStreamName);
                        vo.setNotMemNo(guestNo);

                        try{
                            //socketService.sendGuest(guestNo, roomNo, target.getBjMemNo(), "8", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        }catch(Exception e){}
                        roomCheck = true;*/
                        result.put("status", CommonStatus.??????);
                    }else{
                        result.put("status", CommonStatus.???????????????);
                    }
                }

                //????????? ?????? WEBRTC EDGE ?????? ??????
                /*if(roomCheck && "liveStreamStarted".equals(action)){
                    for(String server : "a".equals(target.getMediaType()) ? WOWZA_AUDIO_SERVER : WOWZA_VIDEO_SERVER) {
                        try {
                            wowzaSocketService.sendFirstEdge(server, streamName);
                        } catch (Exception e) {
                            if("local".equals(DalbitUtil.getActiveProfile())){
                                e.printStackTrace();
                            }
                        }
                    }
                }*/
            }else{
                result.put("status", BroadcastStatus.???????????????_??????);
            }
        }
        return result;
    }

    private HashMap getRouletteData(HttpServletRequest request, String roomNo, int isLogin) {
        RoomOutVo target = getRoomInfo(roomNo, MemberVo.getMyMemNo(request), 1, request);
        if(target != null) {
            //?????? ???????????? ??????
            P_MiniGameVo pMiniGameVo = new P_MiniGameVo();
            pMiniGameVo.setRoom_no(target.getRoomNo());
            pMiniGameVo.setMem_no(target.getBjMemNo());
            pMiniGameVo.setGame_no(1);
            return miniGameService.getMiniGame(pMiniGameVo);
        }

        return null;
    }

    public HashMap doCreateBroadcast(RoomCreateVo roomCreateVo, HttpServletRequest request) throws GlobalException {
        HashMap result = new HashMap();
        result.put("data", new RoomInfoVo());
        /* ????????? ?????? ?????? */
        if(!ipUtil.isInnerIP(ipUtil.getClientIP(request))){
            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.???????????????_???????????????.getCode(), Code.???????????????_???????????????.getDesc()));
            if(!DalbitUtil.isEmpty(codeVo)){
                if(codeVo.getValue().equals("Y")){
                    result.put("status", EventStatus.??????_?????????_??????????????????);
                    return result;
                }
            }
        }

        //???????????? ????????? ??????
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(new DeviceVo(request), MemberVo.getMyMemNo(request)));
        if(0 < adminBlockCnt){
            result.put("status", MemberStatus.??????_????????????);
            return result;
        }

        // ????????????????????? ?????? ( "\r", "\n", "\t")
        if(DalbitUtil.isCheckSlash(roomCreateVo.getTitle())){
            result.put("status", CommonStatus.?????????????????????);
            return result;
        }

        String titleBanWord = commonService.titleBanWordSelect();
        String systemBanWord = commonService.banWordSelect();
        //????????? ??????(??????)
        if(DalbitUtil.isStringMatchCheck(titleBanWord, roomCreateVo.getTitle())){
            result.put("status", CommonStatus.???????????????????????????);
            return result;
        }

        //????????? ??????(?????????)
        if(DalbitUtil.isStringMatchCheck(systemBanWord, roomCreateVo.getWelcomMsg())){
            result.put("status", CommonStatus.??????????????????????????????);
            return result;
        }

        P_RoomCreateVo pRoomCreateVo = new P_RoomCreateVo();
        pRoomCreateVo.setMem_no(MemberVo.getMyMemNo(request));
        pRoomCreateVo.setSubjectType(roomCreateVo.getRoomType());
        pRoomCreateVo.setTitle(roomCreateVo.getTitle());
        pRoomCreateVo.setBackgroundImage(roomCreateVo.getBgImg());
        pRoomCreateVo.setBackgroundImageGrade(DalbitUtil.isStringToNumber(roomCreateVo.getBgImgRacy()));
        pRoomCreateVo.setWelcomMsg(roomCreateVo.getWelcomMsg().replace("\r\n","\n"));
        pRoomCreateVo.setNotice(roomCreateVo.getNotice());
        pRoomCreateVo.setEntryType(roomCreateVo.getEntryType());
        pRoomCreateVo.setImageType(roomCreateVo.getImageType());
        pRoomCreateVo.setMediaType(roomCreateVo.getMediaType());

        DeviceVo deviceVo = new DeviceVo(request);
        pRoomCreateVo.setOs(deviceVo.getOs());
        pRoomCreateVo.setDeviceUuid(deviceVo.getDeviceUuid());
        pRoomCreateVo.setDeviceToken(deviceVo.getDeviceToken());
        pRoomCreateVo.setAppVersion(deviceVo.getAppVersion());
        pRoomCreateVo.setIsWowza(1);
        pRoomCreateVo.setIp(deviceVo.getIp());

        String bgImg = pRoomCreateVo.getBackgroundImage();
        Boolean isDone = false;
        if(DalbitUtil.isEmpty(bgImg)){
            int random = Integer.parseInt(DalbitUtil.randomBgValue());
            bgImg = Code.??????_??????_?????????_PREFIX.getCode()+"/"+Code.???????????????_?????????_PREFIX.getCode()+"200708_"+random+".jpg";
        }else{
            if(bgImg.startsWith(Code.??????_??????_??????_PREFIX.getCode())){
                isDone = true;
            }
            bgImg = DalbitUtil.replacePath(bgImg);
        }

        pRoomCreateVo.setBackgroundImage(bgImg);

        ProcedureVo procedureVo = new ProcedureVo(pRoomCreateVo);
        roomDao.callBroadCastRoomCreate(procedureVo);

        if(procedureVo.getRet().equals(BroadcastStatus.????????????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.isNullToString(resultMap.get("room_no"));
            if(isDone){
                restService.imgDone(DalbitUtil.replaceDonePath(pRoomCreateVo.getBackgroundImage()), request);
            }
            RoomOutVo target = getRoomInfo(roomNo, pRoomCreateVo.getMem_no(), 1, request);
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
            HashMap fanRankMap = commonService.getKingFanRankList(roomNo, request);
            memberInfoVo.setFanRank((List)fanRankMap.get("list"));
            try{
                memberInfoVo.setKingMember((String)fanRankMap.get("kingMemNo"), (String)fanRankMap.get("kingNickNm"), (ImageVo) fanRankMap.get("kingProfImg"));
            }catch(Exception e) {}

            //?????? ??????????????? ?????? ?????? ???????????? ?????? ??????
            P_BroadcastOptionListVo titleListVo = new P_BroadcastOptionListVo();
            titleListVo.setMem_no(MemberVo.getMyMemNo(request));
            String returnTitleCode = mypageService.callBroadcastTitleSelect(titleListVo, "roomCreate");
            if(returnTitleCode.equals(BroadcastStatus.????????????_????????????_??????.getMessageCode())){
                BroadcastOptionAddVo broadcastOptionAddVo = new BroadcastOptionAddVo();
                broadcastOptionAddVo.setContents(pRoomCreateVo.getTitle());
                P_BroadcastTitleAddVo apiData = new P_BroadcastTitleAddVo(broadcastOptionAddVo, request);
                mypageService.callBroadcastTitleAdd(apiData);
            }

            //????????? ??????????????? ?????? ?????? ???????????? ????????? ?????? (????????? ????????????)
            if(!DalbitUtil.isEmpty(pRoomCreateVo.getWelcomMsg())){
                P_BroadcastWelcomeMsgListVo welcomeMsgListVo = new P_BroadcastWelcomeMsgListVo();
                welcomeMsgListVo.setMem_no(MemberVo.getMyMemNo(request));
                String returnWelcomeMsgCode = mypageService.callBroadcastWelcomeMsgSelect(welcomeMsgListVo, "roomCreate");
                if(returnWelcomeMsgCode.equals(BroadcastStatus.????????????_???????????????_??????.getMessageCode())){
                    BroadcastOptionAddVo broadcastOptionAddVo = new BroadcastOptionAddVo();
                    broadcastOptionAddVo.setContents(pRoomCreateVo.getWelcomMsg());
                    P_BroadcastWelcomeMsgAddVo apiData = new P_BroadcastWelcomeMsgAddVo(broadcastOptionAddVo, request);
                    mypageService.callBroadcastWelcomeMsgAdd(apiData);
                }
            }

            //???????????? ?????????????????? + ????????? ??? ?????? ?????? ??????
            //BroadcastSettingEditVo broadcastSettingEditVo = new BroadcastSettingEditVo();
            //broadcastSettingEditVo.setDjListenerIn(roomCreateVo.getDjListenerIn());
            //broadcastSettingEditVo.setDjListenerOut(roomCreateVo.getDjListenerOut());
            //P_BroadcastSettingEditVo pBroadcastSettingEditVo = new P_BroadcastSettingEditVo(broadcastSettingEditVo, request);
            //mypageService.callBroadcastSettingEdit(pBroadcastSettingEditVo, request, "create");

            //???????????? ?????????????????? + ????????? ??? ?????? ?????? ??????
            P_BroadcastSettingVo pBroadcastSettingVo = new P_BroadcastSettingVo(request);
            HashMap settingMap = mypageService.callBroadcastSettingSelectRoomCreate(pBroadcastSettingVo);

            //???????????? ?????? ?????? ??????
            P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
            attendanceCheckVo.setMem_no(MemberVo.getMyMemNo(request));
            int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
            HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);

            // ?????? ?????????
            String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
            customHeader = java.net.URLDecoder.decode(customHeader);
            HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);
            int os = DalbitUtil.getIntMap(headers,"os");
            HashMap miniGameList = null;
            if(os != 3) {
                miniGameList = getRouletteData(request, roomNo, isLogin);
            }

            //????????? ?????? ??????
            RtcTokenBuilder token = new RtcTokenBuilder();
            int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
            String agoraToken = token.buildTokenWithUserAccount(AGORA_APP_ID, AGORA_APP_CERT,
                    roomNo + "", MemberVo.getMyMemNo(request) + "", Role.Role_Publisher, timestamp);
            result.put("status", BroadcastStatus.????????????);
            // tts ?????? ?????????
            ArrayList<Map<String, String>> ttsActors = ttsService.findActor();

            // ????????? ????????? ????????? ??????
            boolean moonLandEvent = false; // [ true: ????????? ?????? o, false: ??????????????? x ]
            List<MoonLandInfoVO> moonLandRound = moonLandService.getMoonLandInfoData();

            if(moonLandRound != null && moonLandRound.size() > 0) {
                moonLandEvent = true;
            }

            RoomInfoVo roomInfoVo = new RoomInfoVo(target, memberInfoVo, WOWZA_PREFIX, settingMap, attendanceCheckMap, deviceVo, miniGameList, ttsActors, moonLandEvent);
            roomInfoVo.setIsGuest(false);
            roomInfoVo.changeBackgroundImg(deviceVo);

            if(badgeService.setBadgeInfo(target.getBjMemNo(), 4)){
                try {
                    log.error("NULL ====> doCreateBroadcast 4 : getBjMemNo {}", target.getBjMemNo());
                    log.error(" NULL ====> doCreateBroadcast  customHeader : {}", customHeader );
                    String referer = request.getHeader("Referer");
                    log.error(" NULL ====> doCreateBroadcast  referer : {}", referer );
                } catch (Exception e) {
                }
            }

            roomInfoVo.setCommonBadgeList(badgeService.getCommonBadge());
            roomInfoVo.setBadgeFrame(badgeService.getBadgeFrame());
            //?????????????????? ???????????? ???????????? DJ??? ??????
            roomInfoVo.setDjTtsSound(DalbitUtil.getBooleanMap(settingMap, "djTtsSound"));
            roomInfoVo.setDjNormalSound(DalbitUtil.getBooleanMap(settingMap, "djNormalSound"));
            roomInfoVo.setTtsSound(DalbitUtil.getBooleanMap(settingMap, "ttsSound"));
            roomInfoVo.setNormalSound(DalbitUtil.getBooleanMap(settingMap, "normalSound"));
            //welcome Event Chk
            //???????????? ????????? ?????? ?????? (memNo ?????? ??????, memSlct [1: dj, 2: ?????????])
            HashMap paramMap = new HashMap<>();
            paramMap.put("memNo", MemberVo.getMyMemNo(request));
            paramMap.put("memSlct", StringUtils.equals(MemberVo.getMyMemNo(request), roomInfoVo.getBjMemNo()) ? 1 : 2);
            roomInfoVo.setEventInfoMap(eventService.broadcastWelcomeUserEventChk(paramMap, deviceVo));
            // ?????? ????????? ????????? ?????? ??????(????????? ????????? ??????, ?????????????????? url)
            roomInfoVo.setStoneEventInfo(dallagersEventService.getBroadcastEventScheduleCheck(request, roomInfoVo.getBjMemNo()));

            /* ???????????? ????????? ?????? */
            roomInfoVo.setSignatureItem(getSignatureItems(target.getBjMemNo(), MemberVo.getMyMemNo(request), deviceVo));

            roomInfoVo.setAgoraToken(agoraToken);
            roomInfoVo.setAgoraAppId(AGORA_APP_ID);
            roomInfoVo.setAgoraAccount(MemberVo.getMyMemNo(request));
            if("v".equals(roomInfoVo.getMediaType())){
                roomInfoVo.setPlatform("agora");
                roomInfoVo.setVideoResolution(720);
                roomInfoVo.setUseFilter(false);
                HashMap agoraMap = new HashMap<>();
                agoraMap.put("agoraArea",AGORA_APP_AREA);
                agoraMap.put("agoraMinVideoBitrate",AGORA_APP_MINBIT);
                agoraMap.put("agoraMaxVideoBitrate",AGORA_APP_MAXBIT);
                agoraMap.put("agoraLightening",AGORA_APP_LIGH);
                agoraMap.put("agoraSmoothness",AGORA_APP_SMOOT);
                agoraMap.put("agoraRedness",AGORA_APP_REDNE);
                agoraMap.put("agoraSharpness",AGORA_APP_SHAR);
                roomInfoVo.setAgoraOption(agoraMap);
            }else{
                roomInfoVo.setPlatform("wowza");
            }
            result.put("data", roomInfoVo);

            //?????? ????????? ????????? ???????????? PUSH ????????????
            try{
                HashMap socketMap = new HashMap();
                socketMap.put("nickNm", roomInfoVo.getBjNickNm());
                socketMap.put("profImg", target.getBjProfImg());
                socketMap.put("roomNo", roomNo + "");
                socketMap.put("msg", roomInfoVo.getBjNickNm()+"?????? ????????? ??????????????????.");
                socketMap.put("memNo", roomInfoVo.getBjMemNo()); // ??????????????? ???????????? ??????

                P_FanNumberVo fanNumberVo = new P_FanNumberVo();
                fanNumberVo.setMem_no(MemberVo.getMyMemNo(request));
                String memNoStr = getFanMemNoList(fanNumberVo);
                if(!DalbitUtil.isEmpty(memNoStr)){
                    socketService.sendRoomCreate(MemberVo.getMyMemNo(request), DalbitUtil.getProperty("socket.global.room"), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), memNoStr);
                }
            }catch(Exception e){
                log.info("Socket Service Create Exception {}", e);
            }

        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.?????????????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.?????????????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_deviceUuid?????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_deviceUuid?????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_20?????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_20?????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_3????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_3????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_20???????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_20???????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_?????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_?????????_????????????);
        } else {
            result.put("status", BroadcastStatus.???????????????);
        }
        return result;
    }

    public HashMap doJoinBroadcast(RoomJoinVo roomJoinVo, HttpServletRequest request) throws GlobalException{

        HashMap result = new HashMap();
        DeviceVo deviceVo = new DeviceVo(request);
        //????????? ?????? ?????? ( PC ?????? )
        if(deviceVo.getOs() == 3 && !DalbitUtil.isLogin(request)) {
            result.put("status", CommonStatus.???????????????);
            return result;
        }else if((deviceVo.getOs() == 1 || deviceVo.getOs() == 2) && DalbitUtil.versionCompare("1.5.7", deviceVo.getAppVersion()) && !DalbitUtil.isLogin(request)){
            result.put("status", CommonStatus.???????????????);
            return result;
        }
        //??? ?????? ?????? ?????? ?????? ??????
        if(!ipUtil.isInnerIP(ipUtil.getClientIP(request))){
            var codeVo = commonService.selectCodeDefine(new CodeVo(Code.???????????????_???????????????.getCode(), Code.???????????????_???????????????.getDesc()));
            if(!DalbitUtil.isEmpty(codeVo)){
                if(codeVo.getValue().equals("Y")){
                    result.put("status", EventStatus.??????_?????????_??????????????????);
                    return result;
                }
            }
        }

        //???????????? ????????? ??????
        int adminBlockCnt = memberDao.selectAdminBlock(new BlockVo(new DeviceVo(request), MemberVo.getMyMemNo(request)));
        if(0 < adminBlockCnt){
            result.put("status", MemberStatus.??????_????????????);
            return result;
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

        pRoomJoinVo.setOs(deviceVo.getOs());
        pRoomJoinVo.setDeviceUuid(deviceVo.getDeviceUuid());
        pRoomJoinVo.setIp(deviceVo.getIp());
        pRoomJoinVo.setAppVersion(deviceVo.getAppVersion());
        pRoomJoinVo.setDeviceToken(deviceVo.getDeviceToken());
        pRoomJoinVo.setIsHybrid(deviceVo.getIsHybrid());

        log.error("callBroadCastRoomJoin prev data(WowzaService) >>>> {}", pRoomJoinVo.toString());
        ProcedureVo procedureVo = new ProcedureVo(pRoomJoinVo);
        roomDao.callBroadCastRoomJoin(procedureVo);

        if(procedureVo.getRet().equals(BroadcastStatus.??????????????????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            RoomOutVo target = getRoomInfo(pRoomJoinVo.getRoom_no(), pRoomJoinVo.getMem_no(), pRoomJoinVo.getMemLogin(), request);
            RoomInfoVo roomInfoVo = getRoomInfo(target, resultMap, request);

            //????????? ?????? ??? ????????????
            if("v".equals(roomInfoVo.getMediaType())){
                if(deviceVo.getOs() == 1 && Integer.parseInt(deviceVo.getAppBuild()) < 53
                        || deviceVo.getOs() == 2 && DalbitUtil.versionCompare("1.5.0", deviceVo.getAppVersion())){
                    P_RoomExitVo apiData = new P_RoomExitVo();
                    apiData.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
                    apiData.setMem_no(MemberVo.getMyMemNo(request));
                    apiData.setRoom_no(roomJoinVo.getRoomNo());
                    apiData.setOs(deviceVo.getOs());
                    apiData.setDeviceUuid(deviceVo.getDeviceUuid());
                    apiData.setIp(deviceVo.getIp());
                    apiData.setAppVersion(deviceVo.getAppVersion());
                    apiData.setDeviceToken(deviceVo.getDeviceToken());
                    apiData.setIsHybrid(deviceVo.getIsHybrid());
                    roomService.callBroadCastRoomExit(apiData, request);
                    ProcedureVo exitVo = new ProcedureVo(apiData);
                    log.error("callBroadcastRoomExit prev data(WowzaService) >>>> {} {} {}", apiData.getMemLogin(), apiData.getMem_no(), apiData.getRoom_no());
                    roomDao.callBroadCastRoomExit(exitVo);

                    result.put("status", CommonStatus.????????????_????????????_??????);
                    return result;
                }
            }

            roomInfoVo.setGuests(getGuestList(roomInfoVo.getRoomNo(), pRoomJoinVo.getMem_no()));
            roomInfoVo.setIsVote(
                voteService.isVote(target.getRoomNo(), target.getBjMemNo(), "s", request)
            );

            //????????? ????????? ?????? ??????
            for (int i=0; i<roomInfoVo.getGuests().size(); i++){
                if(MemberVo.getMyMemNo(request).equals(((RoomGuestListOutVo) roomInfoVo.getGuests().get(i)).getMemNo())){
                    roomInfoVo.setIsGuest(true);
                }else{
                    roomInfoVo.setIsGuest(false);
                }
            }

            SocketVo vo = socketService.getSocketVo(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
            if(!target.isMic() || target.isCall() || !target.isVideo() || !target.isServer()){
                try{
                    //socketService.changeRoomState(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), target.getState(), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), "join", vo);
                    StateEditVo stateEditVo = new StateEditVo();
                    stateEditVo.setRoomNo(pRoomJoinVo.getRoom_no());
                    String mediaState = "";
                    String mediaOn = "";
                    if("a".equals(target.getMediaType())){
                        if(!target.isMic()){
                            mediaState = "mic";
                            mediaOn = "0";
                        }
                    }else{
                        if(!target.isVideo()){
                            mediaState = "video";
                            mediaOn = "0";
                        }
                    }

                    if(target.isCall()){
                        mediaState = "call";
                        mediaOn = "1";
                    }else if(!target.isServer()){
                        mediaState = "server";
                        mediaOn = "0";
                    }

                    stateEditVo.setMediaState(mediaState);
                    stateEditVo.setMediaOn(mediaOn);
                    socketService.roomStateEdit(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), stateEditVo, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo, false, "join");
                    vo.resetData();
                }catch(Exception e){
                    log.info("Socket Service roomStateEdit Exception {}", e);
                }
            }

            try{
                HashMap socketMap = new HashMap();
                socketMap.put("likes", roomInfoVo.getLikes());
                socketMap.put("rank", roomInfoVo.getRank());
                socketMap.put("fanRank", roomInfoVo.getFanRank());
                socketMap.put("newFanCnt", roomInfoVo.getNewFanCnt());
                socketService.changeCount(pRoomJoinVo.getRoom_no(), MemberVo.getMyMemNo(request), socketMap, DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                vo.resetData();
            }catch(Exception e){
                log.info("Socket Service changeCount Exception {}", e);
            }

            /* ???????????? ????????? ????????????, 10??? ?????? ( ?????????????????? ??? ?????? ) */
            roomInfoVo.setSendDalCnt(0);
            roomInfoVo.setSendDalFix(10);
            try{
                Integer sendDalCnt = moonLandService.getUserSendDalCnt(Long.parseLong(pRoomJoinVo.getRoom_no()), Long.parseLong(MemberVo.getMyMemNo(request)) );
                roomInfoVo.setSendDalCnt(sendDalCnt == null? 0 : sendDalCnt);
            } catch (Exception e) {
                log.error("WowzaService.java / doJoinBroadcast / reqUserDalCnt Exception {}", e);
            }

            /* ???????????? ????????? ?????? */
            roomInfoVo.setSignatureItem(getSignatureItems(target.getBjMemNo(), MemberVo.getMyMemNo(request), deviceVo));

            //??????????????? ????????? ?????? ????????? ??????
            //adbrixService("roomJoin", "1151231231312")
            if(target.getFullmoon_yn() == 1){
                if(target.getCompleteMoon() == 0){
                    if(target.getStep() != target.getOldStep()){
                        //????????? ??????
                        HashMap moonCheckMap = new HashMap();
                        moonCheckMap.put("moonStep", target.getMoonCheck().getMoonStep());
                        moonCheckMap.put("moonStepFileNm", target.getMoonCheck().getMoonStepFileNm());
                        moonCheckMap.put("moonStepAniFileNm", target.getMoonCheck().getMoonStepAniFileNm());
                        moonCheckMap.put("dlgTitle", target.getMoonCheck().getDlgTitle());
                        moonCheckMap.put("dlgText", target.getMoonCheck().getDlgText());
                        moonCheckMap.put("aniDuration", target.getMoonCheck().getAniDuration());
                        moonCheckMap.put("fullmoon_yn", target.getFullmoon_yn());

                        String resultCode = actionService.moonCheckSocket(roomJoinVo.getRoomNo(), request, "roomJoin", moonCheckMap);
                        if("error".equals(resultCode)){
                            log.error("????????? ?????? ??????");
                        }
                    }
                }
            }
            //?????? (bjMemNo)??? tts, sound ????????? on/off ?????? ??????
            P_BroadcastSettingVo apiData = new P_BroadcastSettingVo();
            apiData.setMem_no(roomInfoVo.getBjMemNo());
            try {
                HashMap<String, Object> settingMap;
                settingMap = (HashMap<String, Object>) mypageService.callBroadcastSettingSelect(apiData, true);
                roomInfoVo.setDjTtsSound(DalbitUtil.getBooleanMap(settingMap, "djTtsSound"));
                roomInfoVo.setDjNormalSound(DalbitUtil.getBooleanMap(settingMap, "djNormalSound"));

                //????????? (memNo)??? tts, sound ????????? on/off ?????? ?????? (?????????????????? ??????, ???????????? ??????)
                apiData.setMem_no(MemberVo.getMyMemNo(request));
                settingMap = (HashMap<String, Object>) mypageService.callBroadcastSettingSelect(apiData, true);
                roomInfoVo.setTtsSound(DalbitUtil.getBooleanMap(settingMap, "ttsSound"));
                roomInfoVo.setNormalSound(DalbitUtil.getBooleanMap(settingMap, "normalSound"));
            }catch(Exception e){
                log.error("WowzaService roomJoin callBroadcastSettingSelect => {}", e);
                roomInfoVo.setTtsSound(true);
                roomInfoVo.setNormalSound(true);
                roomInfoVo.setDjTtsSound(true);
                roomInfoVo.setDjNormalSound(true);
            }

            //???????????? ????????? ?????? ?????? (memNo ?????? ??????, memSlct [1: dj, 2: ?????????])
            HashMap paramMap = new HashMap<>();
            paramMap.put("memNo", MemberVo.getMyMemNo(request));
            paramMap.put("memSlct", StringUtils.equals(MemberVo.getMyMemNo(request), roomInfoVo.getBjMemNo()) ? 1 : 2);
            roomInfoVo.setEventInfoMap(eventService.broadcastWelcomeUserEventChk(paramMap, deviceVo));
            //?????? ????????? ????????? ?????? ??????(????????? ????????? ??????, ?????????????????? url)
            roomInfoVo.setStoneEventInfo(dallagersEventService.getBroadcastEventScheduleCheck(request, roomInfoVo.getBjMemNo()));

            //????????? ?????? ??????
            RtcTokenBuilder token = new RtcTokenBuilder();
            int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
            String agoraToken = token.buildTokenWithUserAccount(AGORA_APP_ID, AGORA_APP_CERT,
                    roomInfoVo.getRoomNo() + "", MemberVo.getMyMemNo(request) + "", Role.Role_Subscriber, timestamp);
            roomInfoVo.setAgoraToken(agoraToken);
            roomInfoVo.setAgoraAppId(AGORA_APP_ID);
            roomInfoVo.setAgoraAccount(MemberVo.getMyMemNo(request));
            if("v".equals(roomInfoVo.getMediaType())){
                roomInfoVo.setPlatform("agora");
                roomInfoVo.setVideoResolution(720);
                roomInfoVo.setUseFilter(false);
                HashMap agoraMap = new HashMap<>();
                agoraMap.put("agoraArea",AGORA_APP_AREA);
                agoraMap.put("agoraMinVideoBitrate",AGORA_APP_MINBIT);
                agoraMap.put("agoraMaxVideoBitrate",AGORA_APP_MAXBIT);
                agoraMap.put("agoraLightening",AGORA_APP_LIGH);
                agoraMap.put("agoraSmoothness",AGORA_APP_SMOOT);
                agoraMap.put("agoraRedness",AGORA_APP_REDNE);
                agoraMap.put("agoraSharpness",AGORA_APP_SHAR);
                roomInfoVo.setAgoraOption(agoraMap);
            }else{
                roomInfoVo.setPlatform("wowza");
            }
            roomInfoVo.changeBackgroundImg(deviceVo);
            result.put("status", BroadcastStatus.??????????????????);
            result.put("data", roomInfoVo);
            // tts ?????? ?????????
//            ArrayList<Map<String, String>> ttsActors = ttsService.findActor();
//            result.put("ttsActors", ttsActors);

        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_??????????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_??????????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_???????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_???????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String auth = DalbitUtil.getStringMap(resultMap, "auth");
            CodeVo codeVo1 = commonService.getCodeList("roomRight").stream().filter(code -> code.getCdNm().equals("??????")).findFirst().orElse(null);
            if((!DalbitUtil.isEmpty(codeVo1) && auth.equals(codeVo1.getCd()))){ //????????? ????????? ??????
                result.put("status", BroadcastStatus.????????????_?????????);
            }else{
                String deviceUuid = DalbitUtil.getStringMap(resultMap, "deviceUuid");
                deviceUuid = deviceUuid == null ? "" : deviceUuid.trim();

                if(deviceUuid.equals(pRoomJoinVo.getDeviceUuid())){ //???????????? ???????????? /reToken??? ????????????
                    RoomTokenVo roomTokenVo = new RoomTokenVo();
                    roomTokenVo.setRoomNo(roomJoinVo.getRoomNo());
                    result = getBroadcast(roomTokenVo, request);
                }else{
                    result.put("status", BroadcastStatus.????????????_????????????);
                }
            }
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_??????????????????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            HashMap data = new HashMap();
            data.put("remainTime", DalbitUtil.getIntMap(resultMap, "remainTime"));
            result.put("status", BroadcastStatus.????????????_??????????????????);
            result.put("data", data);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_???????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_???????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_?????????IP??????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_?????????IP??????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_????????????????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_????????????????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_20?????????????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_20?????????????????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.?????????_?????????.getMessageCode())) {
            result.put("status", BroadcastStatus.?????????_?????????);
        } else if (procedureVo.getRet().equals(BroadcastStatus.????????????_?????????_???????????????.getMessageCode())) {
            result.put("status", BroadcastStatus.????????????_?????????_???????????????);
        } else {
            result.put("status", BroadcastStatus.???????????????);
        }
        return result;
    }

    public HashMap getBroadcast(RoomTokenVo roomTokenVo, HttpServletRequest request) throws GlobalException{
        HashMap result = new HashMap();
        RoomOutVo target = getRoomInfo(roomTokenVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request), request);
        if(target != null){
            if(target.getState() == 4){
                result.put("status", BroadcastStatus.???????????????_???????????????);
            }else{
                P_RoomStreamTokenVo pRoomStreamTokenVo = new P_RoomStreamTokenVo();
                pRoomStreamTokenVo.setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
                pRoomStreamTokenVo.setMem_no(MemberVo.getMyMemNo(request));
                pRoomStreamTokenVo.setRoom_no(roomTokenVo.getRoomNo());
                pRoomStreamTokenVo.setBj_streamid(WOWZA_PREFIX + roomTokenVo.getRoomNo());
                ProcedureVo procedureUpdateVo = new ProcedureVo(pRoomStreamTokenVo);
                roomDao.callBroadcastRoomTokenUpdate(procedureUpdateVo);
                if(BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())) {
                    HashMap resultUpdateMap = new Gson().fromJson(procedureUpdateVo.getExt(), HashMap.class);
                    String deviceUuid = StringUtils.defaultIfEmpty(DalbitUtil.getStringMap(resultUpdateMap, "deviceUuid"), "").trim();
                    int auth = DalbitUtil.getIntMap(resultUpdateMap, "auth");
                    DeviceVo deviceVo = new DeviceVo(request);
                    if(!deviceUuid.equals(deviceVo.getDeviceUuid())){ //?????? ????????? ?????????,?????? ?????? ????????? ??????
                        if(auth == 3){
                            if(target.getState() != 5){
                                result.put("status", BroadcastStatus.????????????_?????????);
                                return result;
                            }
                        }else{
                            result.put("status", BroadcastStatus.????????????_????????????);
                            return result;
                        }
                    }

                    RoomInfoVo roomInfoVo = getRoomInfo(target, resultUpdateMap, request);
                    roomInfoVo.setGuests(getGuestList(roomInfoVo.getRoomNo(), MemberVo.getMyMemNo(request)));
                    //????????? ?????? ??????
                    RtcTokenBuilder token = new RtcTokenBuilder();
                    int timestamp = (int)(System.currentTimeMillis() / 1000 + expirationTimeInSeconds);
                    String agoraToken = token.buildTokenWithUserAccount(AGORA_APP_ID, AGORA_APP_CERT,
                            roomTokenVo.getRoomNo() + "", MemberVo.getMyMemNo(request) + "", Role.Role_Subscriber, timestamp);;
                    roomInfoVo.setAgoraToken(agoraToken);
                    roomInfoVo.setAgoraAppId(AGORA_APP_ID);
                    roomInfoVo.setAgoraAccount(MemberVo.getMyMemNo(request));
                    roomInfoVo.setIsVote(
                            voteService.isVote(target.getRoomNo(), target.getBjMemNo(), "s", request)
                    );
                    if("v".equals(roomInfoVo.getMediaType())){
                        roomInfoVo.setPlatform("agora");
                        roomInfoVo.setVideoResolution(720);
                        roomInfoVo.setUseFilter(false);
                        HashMap agoraMap = new HashMap<>();
                        agoraMap.put("agoraArea",AGORA_APP_AREA);
                        agoraMap.put("agoraMinVideoBitrate",AGORA_APP_MINBIT);
                        agoraMap.put("agoraMaxVideoBitrate",AGORA_APP_MAXBIT);
                        agoraMap.put("agoraLightening",AGORA_APP_LIGH);
                        agoraMap.put("agoraSmoothness",AGORA_APP_SMOOT);
                        agoraMap.put("agoraRedness",AGORA_APP_REDNE);
                        agoraMap.put("agoraSharpness",AGORA_APP_SHAR);
                        roomInfoVo.setAgoraOption(agoraMap);
                    }else{
                        roomInfoVo.setPlatform("wowza");
                    }

                    //????????? ????????? ?????? ??????????????? ??????
                    for (int i=0; i < roomInfoVo.getGuests().size(); i++ ){
                        if(MemberVo.getMyMemNo(request).equals(((RoomGuestListOutVo) roomInfoVo.getGuests().get(i)).getMemNo())){
                            roomInfoVo.setIsGuest(true);
                        }else{
                            roomInfoVo.setIsGuest(false);
                        }
                    }

                    //??? ??????????????? F5 ???????????? roomInfo ?????????
                    try {
                        P_BroadcastSettingVo apiData = new P_BroadcastSettingVo();
                        apiData.setMem_no(roomInfoVo.getBjMemNo());
                        HashMap<String, Object> settingMap;
                        settingMap = (HashMap<String, Object>) mypageService.callBroadcastSettingSelect(apiData, true);
                        roomInfoVo.setDjTtsSound(DalbitUtil.getBooleanMap(settingMap, "djTtsSound"));
                        roomInfoVo.setDjNormalSound(DalbitUtil.getBooleanMap(settingMap, "djNormalSound"));

                        //????????? (memNo)??? tts, sound ????????? on/off ?????? ?????? (?????????????????? ??????, ???????????? ??????)
                        apiData.setMem_no(MemberVo.getMyMemNo(request));
                        settingMap = (HashMap<String, Object>) mypageService.callBroadcastSettingSelect(apiData, true);
                        roomInfoVo.setTtsSound(DalbitUtil.getBooleanMap(settingMap, "ttsSound"));
                        roomInfoVo.setNormalSound(DalbitUtil.getBooleanMap(settingMap, "normalSound"));

                    } catch (Exception e) {
                        log.error("WowzaService roomInfo callBroadcastSettingSelect => {}", e);
                        roomInfoVo.setTtsSound(true);
                        roomInfoVo.setNormalSound(true);
                        roomInfoVo.setDjTtsSound(true);
                        roomInfoVo.setDjNormalSound(true);
                    }

                    //welcome event chk
                    //???????????? ????????? ?????? ?????? (memNo ?????? ??????, memSlct [1: dj, 2: ?????????])
                    HashMap paramMap = new HashMap<>();
                    paramMap.put("memNo", MemberVo.getMyMemNo(request));
                    paramMap.put("memSlct", StringUtils.equals(MemberVo.getMyMemNo(request), roomInfoVo.getBjMemNo()) ? 1 : 2);
                    roomInfoVo.setEventInfoMap(eventService.broadcastWelcomeUserEventChk(paramMap, deviceVo));

                    //?????? ????????? ????????? ?????? ??????(????????? ????????? ??????, ?????????????????? url)
                    roomInfoVo.setStoneEventInfo(dallagersEventService.getBroadcastEventScheduleCheck(request, roomInfoVo.getBjMemNo()));
                    roomInfoVo.changeBackgroundImg(deviceVo);

                    /* ???????????? ????????? ????????????, 10??? ?????? ( ?????????????????? ??? ?????? ) */
                    roomInfoVo.setSendDalCnt(0);
                    roomInfoVo.setSendDalFix(10);
                    try{
                        Integer sendDalCnt = moonLandService.getUserSendDalCnt(Long.valueOf(roomTokenVo.getRoomNo()), Long.valueOf(MemberVo.getMyMemNo(request)) );
                        roomInfoVo.setSendDalCnt(sendDalCnt == null? 0 : sendDalCnt);
                    } catch (Exception e) {
                        log.error("WowzaService.java / getBroadcast() / reqUserDalCnt Exception {}", e);
                    }

                    /* ???????????? ????????? ?????? */
                    roomInfoVo.setSignatureItem(getSignatureItems(target.getBjMemNo(), MemberVo.getMyMemNo(request), deviceVo));

                    result.put("status", BroadcastStatus.???????????????);
                    result.put("data", roomInfoVo);
                }else if(BroadcastStatus.??????????????????_????????????.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", BroadcastStatus.??????????????????_????????????);
                }else if(BroadcastStatus.??????????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", BroadcastStatus.??????????????????_???????????????);
                }else if(BroadcastStatus.??????????????????_????????????_???????????????.getMessageCode().equals(procedureUpdateVo.getRet())){
                    result.put("status", BroadcastStatus.??????????????????_????????????_???????????????);
                }else{
                    result.put("status", BroadcastStatus.???????????????_??????);
                }
            }
        }else{
            result.put("status", BroadcastStatus.???????????????_??????);
        }
        return result;
    }

    public RoomOutVo getRoomInfo(String roomNo, HttpServletRequest request){
        return getRoomInfo(roomNo, "0", 99, request);
    }

    public RoomOutVo getRoomInfo(String roomNo, String memNo, boolean isLogin, HttpServletRequest request) {
        return getRoomInfo(roomNo, memNo, (isLogin ? 1 : 0), request);
    }

    public RoomOutVo getRoomInfo(String roomNo, String memNo, int isLogin, HttpServletRequest request) {
        P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
        pRoomInfoViewVo.setMemLogin(isLogin);
        pRoomInfoViewVo.setMem_no(memNo);
        pRoomInfoViewVo.setRoom_no(roomNo);
        ProcedureVo procedureInfoViewVo = new ProcedureVo(pRoomInfoViewVo);
        P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureInfoViewVo);
         if (procedureInfoViewVo.getRet().equals(BroadcastStatus.???????????????.getMessageCode())){
            roomInfoViewVo.setExt(procedureInfoViewVo.getExt());
            //???????????? ?????? ?????? ??????
            P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo();
            attendanceCheckVo.setMem_no(memNo);
            HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);

            HashMap moonCheckMap = new HashMap();
            //????????? ????????????
            if(roomInfoViewVo.getFullmoon_yn() == 1){
                //????????? ??????
                P_MoonCheckVo pMoonCheckVo = new P_MoonCheckVo();
                pMoonCheckVo.setRoom_no(roomNo);
                moonCheckMap = actionService.callMoonCheckMap(pMoonCheckVo, request);
                if(roomInfoViewVo.getCompleteMoon() > 0){
                    var codeVo = commonService.selectCodeDefine(new CodeVo(Code.?????????_??????.getCode(), "4"));
                    moonCheckMap.put("moonStep", 4);
                    if(!DalbitUtil.isEmpty(codeVo)){
                        String code = Code.?????????_???????????????.getCode();
                        String value = DalbitUtil.randomMoonAniValue();
                        if(String.valueOf(roomInfoViewVo.getCompleteMoon()).equals(EventStatus.?????????_??????.getMessageCode())){
                            code = Code.?????????_???????????????.getCode();
                            value = "1";
                        }
                        var aniCodeVo = commonService.selectCodeDefine(new CodeVo(code, value));
                        moonCheckMap.put("moonStepFileNm", aniCodeVo.getValue());
                        moonCheckMap.put("aniDuration", aniCodeVo.getSortNo());
                        if(!DalbitUtil.isEmpty(aniCodeVo)) {
                            moonCheckMap.put("moonStepAniFileNm", "");
                            moonCheckMap.put("aniDuration", aniCodeVo.getSortNo());
                        }
                    }
                    moonCheckMap.put("dlgTitle", DalbitUtil.getStringMap(moonCheckMap, "dlgTitle"));
                    moonCheckMap.put("dlgText", DalbitUtil.getStringMap(moonCheckMap, "dlgText"));
                }
            }else{
                moonCheckMap.put("moonStepFileNm", "");
                moonCheckMap.put("moonStepAniFileNm", "");
                moonCheckMap.put("aniDuration", 0);
                moonCheckMap.put("dlgTitle", "");
                moonCheckMap.put("dlgText", "");
                moonCheckMap.put("moonStep", 4);
            }

            boolean isMiniGame = false;
            var minigame = commonService.selectCodeDefine(new CodeVo(Code.????????????_????????????.getCode(), Code.????????????_????????????.getDesc()));
            if(!DalbitUtil.isEmpty(minigame)){
                if("1".equals(minigame.getValue())){
                    isMiniGame = true;
                }
            }
            return new RoomOutVo(roomInfoViewVo, attendanceCheckMap, moonCheckMap, isMiniGame);
        }
        return null;
    }

    public RoomInfoVo getRoomInfo(RoomOutVo target, HttpServletRequest request){
        return getRoomInfo(target, null, request);
    }
    public RoomInfoVo getRoomInfo(RoomOutVo target, HashMap resultMap, HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo(request);
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
        memberInfoVo.setUseBoost(roomService.existsBoostByRoom(target.getRoomNo(), memNo));
        HashMap fanRankMap = commonService.getKingFanRankList(target.getRoomNo(), request);
        memberInfoVo.setFanRank((List)fanRankMap.get("list"));
        try{
            memberInfoVo.setKingMember((String)fanRankMap.get("kingMemNo"), (String)fanRankMap.get("kingNickNm"), (ImageVo) fanRankMap.get("kingProfImg"));
        }catch(Exception e) {}

        //???????????? ?????????????????? + ????????? ??? ?????? ?????? ??????
        P_BroadcastSettingVo pBroadcastSettingVo = new P_BroadcastSettingVo(request);
        HashMap settingMap = mypageService.callBroadcastSettingSelectRoomCreate(pBroadcastSettingVo);

        //???????????? ?????? ?????? ??????
        P_AttendanceCheckVo attendanceCheckVo = new P_AttendanceCheckVo(request);
        attendanceCheckVo.setMem_no(memNo);
        int isLogin = DalbitUtil.isLogin(request) ? 1 : 0;
        HashMap attendanceCheckMap = eventService.callAttendanceCheckMap(isLogin, attendanceCheckVo);

        //?????? ?????? ?????? ????????? ??????
        if(!memNo.equals(target.getBjMemNo())){
            P_BehaviorRandomVo apiData = new P_BehaviorRandomVo();
            apiData.setMem_no(memNo);
            apiData.setDj_mem_no(target.getBjMemNo());
            memberInfoVo.setRandomMsgList(getBehaviorRandomMsgList(apiData));
        }

        //?????? ???????????? ??????
        HashMap miniGameMap = getRouletteData(request, target.getRoomNo(), isLogin);

        // tts ?????? ?????????
        ArrayList<Map<String, String>> ttsActors = ttsService.findActor();

        // ????????? ????????? ????????? ??????
        boolean moonLandEvent = false; // [ true: ????????? ?????? o, false: ??????????????? x ]
        List<MoonLandInfoVO> moonLandRound = moonLandService.getMoonLandInfoData();
        if (moonLandRound != null && moonLandRound.size() > 0) { // ?????????????????? ????????? ????????? ?????????!
            moonLandEvent = true;
        }

        RoomInfoVo roomInfoVo = new RoomInfoVo(target, memberInfoVo, WOWZA_PREFIX, settingMap, attendanceCheckMap, new DeviceVo(request), miniGameMap, ttsActors, moonLandEvent);

        if(badgeService.setBadgeInfo(target.getBjMemNo(), 4)){
            try {
                log.error("NULL ====> getRoomInfo 4 : getTarget_mem_no {}", target.getBjMemNo());
                String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
                customHeader = java.net.URLDecoder.decode(customHeader);
                log.error(" NULL ====> getRoomInfo  customHeader : {}", customHeader );
                String referer = request.getHeader("Referer");
                log.error(" NULL ====> getRoomInfo  referer : {}", referer );
            } catch (Exception e) {
            }
        }
        roomInfoVo.setCommonBadgeList(badgeService.getCommonBadge());
        roomInfoVo.setBadgeFrame(badgeService.getBadgeFrame());
        roomInfoVo.setJoinDate(DalbitUtil.getStringMap(resultMap, "joinDate"));
        return roomInfoVo;
    }

    public List getGuestList(String roomNo, String memNo){
        List list = new ArrayList();
        GuestListVo guestListVo = new GuestListVo();
        guestListVo.setRoomNo(roomNo);
        List<P_GuestListVo> pGuestListVo = guestDao.selectGuestList(guestListVo);
        if(!DalbitUtil.isEmpty(pGuestListVo)){
            for (int i=0; i<pGuestListVo.size(); i++){
                RoomGuestListOutVo roomGuestListOut = new RoomGuestListOutVo(pGuestListVo.get(i), memNo, WOWZA_PREFIX);
                list.add(roomGuestListOut);
            }
        }
        return list;
    }

    public HashMap doContinueBroadcast(HttpServletRequest request) throws GlobalException {
        P_BroadContinueVo pBroadContinueVo = new P_BroadContinueVo(request);
        ProcedureVo procedureVo = new ProcedureVo(pBroadContinueVo);
        roomDao.callBroadCastRoomContinue(procedureVo);

        HashMap result = new HashMap();
        if(procedureVo.getRet().equals(BroadcastStatus.????????????_??????.getMessageCode())) {
            HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
            String roomNo = DalbitUtil.isNullToString(resultMap.get("room_no"));
            RoomTokenVo roomTokenVo = new RoomTokenVo();
            roomTokenVo.setRoomNo(roomNo);
            HashMap roomInfo = getBroadcast(roomTokenVo, request);
            if(roomInfo.containsKey("data")){
                RoomInfoVo roomInfoVo = (RoomInfoVo)roomInfo.get("data");
                if(!DalbitUtil.isEmpty(roomInfoVo)){
                    SocketVo vo = socketService.getSocketVo(roomInfoVo.getRoomNo(), MemberVo.getMyMemNo(request), DalbitUtil.isLogin(request));
                    try{
                        socketService.roomChangeTime(roomInfoVo.getRoomNo(), roomInfoVo.getBjMemNo(), DalbitUtil.getStringMap(resultMap, "extendEndDate"), DalbitUtil.getAuthToken(request), DalbitUtil.isLogin(request), vo);
                        vo.resetData();
                    }catch(Exception e){}
                }
            }
            return roomInfo;
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_????????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_????????????);
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_?????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_?????????);
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_???????????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_???????????????);
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_????????????5?????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_????????????5?????????);
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_????????????5?????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_????????????5?????????);
        }else if(procedureVo.getRet().equals(BroadcastStatus.????????????_?????????_????????????.getMessageCode())){
            result.put("status", BroadcastStatus.????????????_?????????_????????????);
        }else{
            result.put("status", BroadcastStatus.????????????_??????);
        }
        return result;
    }

    public HashMap doUpdateStateNormalize(HttpServletRequest request) throws GlobalException {
        P_BroadStateNormalize pBroadStateNormalize = new P_BroadStateNormalize(request);
        ProcedureVo procedureVo = new ProcedureVo(pBroadStateNormalize);
        roomDao.callRoomStateNormalize(procedureVo);

        HashMap result = new HashMap();
        if("0".equals(procedureVo.getRet())){
            RoomTokenVo roomTokenVo = new RoomTokenVo();
            roomTokenVo.setRoomNo(pBroadStateNormalize.getRoom_no());
            result = getBroadcast(roomTokenVo, request);
        }else{
            result.put("status", BroadcastStatus.????????????_?????????);
        }

        return result;
    }

    public String getFanMemNoList(P_FanNumberVo pFanNumberVo){
        ProcedureVo procedureVo = new ProcedureVo(pFanNumberVo);
        memberDao.getFanMemNoList(procedureVo);
        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        String memNoStr = DalbitUtil.getStringMap(resultMap, "memNoList");
        return memNoStr;
    }

    public List getBehaviorRandomMsgList(P_BehaviorRandomVo pBehaviorRandomVo){
        ProcedureVo procedureVo = new ProcedureVo(pBehaviorRandomVo);
        List<RandomMsgVo> randomMsgList = memberDao.getBehaviorRandomMsgList(procedureVo);
        if(DalbitUtil.isEmpty(randomMsgList)){
            randomMsgList = new ArrayList<>();
        }
        return randomMsgList;
    }

    /* ???????????? ?????? ?????? */
    public HashMap getSignatureItems(String bjMemNo, String userMemNo, DeviceVo deviceVo) {
        HashMap map = new HashMap();
        try {
            if (!StringUtils.equals(bjMemNo, "") && bjMemNo != null) {
                HashMap param = new HashMap();
                param.put("memNo", bjMemNo);

                List<ItemVo> items = broadcast.spSignatureItemSelect(param);

                if (items != null) {
                    ArrayList<ItemCategoryVo> list = new ArrayList();
                    List<ItemVo> tempItems = new ArrayList<>();
                    ItemCategoryVo itemCategoryVo = new ItemCategoryVo("signature", "????????????", false);
                    boolean signitureCategoriesFlag = false;

                    if(!DalbitUtil.isEmpty(items)){
                        for(int i = 0; i < items.size(); i++){
                            if(deviceVo.getOs() == 3){
                                items.get(i).setWebpUrl(StringUtils.replace(items.get(i).getWebpUrl(), "_1X", "_2X"));
                            }
                            if(items.get(i).isNew()){
                                itemCategoryVo.setIsNew(true);
                            }
                            if (items.get(i).getView_yn().equals(1)) {
                                tempItems.add(items.get(i));
                                signitureCategoriesFlag = true;
                            }
                        }
                        
                        // view_yn : 1 ??? ????????? 1??? ??????
                        if(signitureCategoriesFlag) {
                            list.add(itemCategoryVo);
                        }
                    }

                    map.put("itemCategories", list);
                    map.put("items", tempItems);
                } else {
                    log.error("WowzaService.java / getSignatureItem() => DB return null, bjMemNo: {}, userMemNo: {}", bjMemNo, userMemNo);
                    map.put("itemCategories", new ArrayList());
                    map.put("items", new ArrayList());
                }
            } else {
                log.error("WowzaService.java / getSignatureItem() => bjMemNo : {}, userMemNo : {}", bjMemNo, userMemNo);
            }
        } catch (Exception e) {
            log.error("WowzaService.java / getSignatureItem() => Exception - bjMemNo : {}, userMemNo : {}, error : {}", bjMemNo, userMemNo, e);
            map.put("items", new ArrayList());
            map.put("itemCategories", new ArrayList());
        }

        return map;
    }
}

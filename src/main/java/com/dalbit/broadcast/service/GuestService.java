package com.dalbit.broadcast.service;

import com.dalbit.broadcast.dao.GuestDao;
import com.dalbit.broadcast.dao.UserDao;
import com.dalbit.broadcast.vo.GuestInfoVo;
import com.dalbit.broadcast.vo.RoomGuestListOutVo;
import com.dalbit.broadcast.vo.RoomInfoVo;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.procedure.*;
import com.dalbit.broadcast.vo.request.GuestListOutVo;
import com.dalbit.broadcast.vo.request.GuestListVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.*;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.rest.service.RestService;
import com.dalbit.socket.service.SocketService;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class GuestService {
    @Autowired
    UserDao userDao;
    @Autowired
    GuestDao guestDao;
    @Autowired
    RestService restService;
    @Autowired
    GsonUtil gsonUtil;
    @Autowired
    SocketService socketService;
    @Autowired
    GuestService guestService;
    @Autowired
    CommonService commonService;
    @Autowired
    WowzaService wowzaService;

    @Value("${wowza.prefix}")
    String WOWZA_PREFIX;
    @Value("${wowza.real.server}")
    String[] WOWZA_REALSERVER;


    /**
     * 게스트 통합
     */
    public String guest(HttpServletRequest request) {
        String roomNo = request.getParameter("roomNo");
        String memNo = request.getParameter("memNo"); //게스트 회원번호
        String mode = request.getParameter("mode");

        Status status = null;
        HashMap selParams = new HashMap();
        selParams.put("mem_no", memNo);
        selParams.put("room_no", roomNo);
        HashMap roomGuestInfo = userDao.selectGuestStreamInfo(selParams);
        if(roomGuestInfo == null){
            return gsonUtil.toJson(new JsonOutputVo(Status.데이터없음, null));
        }

        GuestInfoVo guestInfoVo = new GuestInfoVo();
        guestInfoVo.setMode(Integer.parseInt(mode));
        guestInfoVo.setMemNo(memNo);
        guestInfoVo.setNickNm((String) roomGuestInfo.get("mem_nick"));
        guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String)roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));
        guestInfoVo.setMsg("");
        boolean isGuest = true;

        if(!DalbitUtil.isEmpty(roomNo) && !DalbitUtil.isEmpty(memNo) && !DalbitUtil.isEmpty(mode)){
            if("1".equals(mode) || "2".equals(mode) || "3".equals(mode) || "4".equals(mode) || "5".equals(mode) || "6".equals(mode) || "7".equals(mode) || "8".equals(mode) || "9".equals(mode) || "10".equals(mode)){

                String djMemNo = DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no");
                P_GuestInviteVo inviteVo = new P_GuestInviteVo(memNo, roomNo, request);
                if("1".equals(mode)) { // 초대
                    status = guestService.callGuestInvite(inviteVo);

                }else if("2".equals(mode)){ // 초대취소
                    status = guestService.callGuestInviteCancel(inviteVo);

                }else if("3".equals(mode) || "4".equals(mode) || "5".equals(mode)){ // 수락 | 거절 | 신청 방장번호 필요
                    if(roomGuestInfo != null /*&& !DalbitUtil.isEmpty(DalbitUtil.getStringMap(roomGuestInfo, "title"))*/){
                        if("3".equals(mode)) { // 수락
                            try{
                                P_GuestInviteOkVo inviteOkVo = new P_GuestInviteOkVo(roomNo, 1, request);
                                status = guestService.callGuestInviteOk(inviteOkVo);

                                if(status.equals(Status.게스트초대수락_성공)){
                                    //게스트 지정을 위한 방송방 정보 조회
                                    RoomOutVo roomOutVo = wowzaService.getRoomInfo(roomNo, memNo, DalbitUtil.isLogin(request) ? 1 : 0);
                                    RoomInfoVo roomInfoVo = wowzaService.getRoomInfo(roomOutVo, request);

                                    String rtmpOrigin = roomInfoVo.getRtmpOrigin();             //WOWZA RMTP ORIGIN URL (NATIVE)
                                    String rtmpEdge =  roomInfoVo.getRtmpEdge();                //WOWZA RMTP EDGE URL (NATIVE)
                                    String webRtcUrl = roomInfoVo.getWebRtcUrl();               //WOWZA WebRtc URL (PC)
                                    String webRtcAppName = roomInfoVo.getWebRtcAppName();       //WOWZA 앱네임 (PC)
                                    String webRtcStreamName = roomInfoVo.getWebRtcStreamName(); //WOWZA 스트림 네임 (PC)  ( DEV or REAL + room_no)

                                    String gstWebRtcStreamName = WOWZA_PREFIX+roomNo+"_"+memNo; //WOWZA 스트림 네임 (PC)  ( DEV or REAL + room_no + _ + mem_no)
                                    String gstRtmpOrigin = DalbitUtil.getProperty("wowza.rtmp.origin") + "/" + gstWebRtcStreamName;// + "_aac";
                                    String gstRtmpEdge =  DalbitUtil.getProperty("wowza.rtmp.edge") + "/" + gstWebRtcStreamName + "_aac";
                                    String gstWebRtcUrl = DalbitUtil.getProperty("wowza.wss.url") ;
                                    String gstWebRtcAppName = roomInfoVo.getAuth() == 2 ? "origin" : "edge";
                                    if(memNo.equals(MemberVo.getMyMemNo(request))){ //게스트가 본인이면 origin
                                        gstWebRtcAppName = "origin";
                                    }else{ // 게스트가 본인이 아니라면 edge
                                        gstWebRtcAppName = "edge";
                                        gstWebRtcStreamName = WOWZA_PREFIX+roomNo+"_"+memNo+"_opus";
                                    }

                                    //게스트 지정
                                    P_RoomGuestVo roomGuestVo = new P_RoomGuestVo(DalbitUtil.getStringMap(roomGuestInfo, "dj_mem_no"), memNo, roomNo, webRtcStreamName, rtmpEdge, rtmpOrigin, gstWebRtcStreamName, gstRtmpEdge, gstRtmpOrigin, request);
                                    status = guestService.callBroadCastRoomGuestAdd(roomGuestVo, request);

                                    if(status.equals(Status.게스트지정)){
                                        guestInfoVo.setNickNm(DalbitUtil.getStringMap(roomGuestInfo, "mem_nick"));
                                        guestInfoVo.setProfImg(new ImageVo(roomGuestInfo.get("image_profile"), (String)roomGuestInfo.get("mem_sex"), DalbitUtil.getProperty("server.photo.url")));
                                        guestInfoVo.setRtmpOrigin(gstRtmpOrigin);
                                        guestInfoVo.setRtmpEdge(gstRtmpEdge);
                                        guestInfoVo.setWebRtcUrl(gstWebRtcUrl);
                                        guestInfoVo.setWebRtcAppName(gstWebRtcAppName);
                                        guestInfoVo.setWebRtcStreamName(gstWebRtcStreamName);
                                    }
                                }
                            }catch(Exception e){
                                status = Status.비즈니스로직오류;
                            }
                        }else if ("4".equals(mode)){ // 거절
                            P_GuestInviteOkVo inviteOkVo = new P_GuestInviteOkVo(roomNo, 0, request);
                            status = guestService.callGuestInviteOk(inviteOkVo);
                            guestInfoVo.setNickNm(DalbitUtil.getStringMap(roomGuestInfo, "mem_nick"));

                        }else if ("5".equals(mode)){ // 신청
                            P_GuestProposeVo proposeVo = new P_GuestProposeVo(roomNo, request);
                            return guestService.callGuestPropose(proposeVo, memNo, roomNo, djMemNo, mode, guestInfoVo.getNickNm(), request, DalbitUtil.getAuthToken(request));
                        }
                    }else{
                        status = Status.방송참여_해당방이없음;
                    }
                }else if("6".equals(mode) || "9".equals(mode)){ // 6:퇴장(게스트 취소) = 연결종료, 9:비정상종료
                    P_RoomGuestVo apiData = new P_RoomGuestVo(djMemNo, memNo, roomNo, "", "", "", "", "", "", request);
                    status =  guestService.callBroadCastRoomGuestCancel(apiData, request);

                }else if("7".equals(mode)){ // 신청취소
                    P_GuestProposeVo proposeCancelVo = new P_GuestProposeVo(roomNo, request);
                    return guestService.callGuestProposeCancel(proposeCancelVo, memNo, roomNo, djMemNo, mode, guestInfoVo.getNickNm(), request, DalbitUtil.getAuthToken(request));

                }else if("8".equals(mode)){ // 방송연결
                    P_GuestListVo apiData = new P_GuestListVo();
                    apiData.setRoom_no(roomNo);
                    apiData.setMem_no(memNo);
                    List<P_GuestListVo> guestListVo = guestService.getGuestInfo(apiData);
                    if(DalbitUtil.isEmpty(guestListVo)){
                        isGuest = false;
                    }else {
                        for (int i = 0; i < guestListVo.size(); i++) {
                            if (guestListVo.get(i).getGuest_yn() != 1) {
                                isGuest = false;
                            }
                        }
                    }

                    String streamName = this.WOWZA_PREFIX + roomNo + "_" + guestInfoVo.getMemNo();
                    String gstRtmpOrigin = DalbitUtil.getProperty("wowza.rtmp.origin") + "/" + streamName;// + "_aac";
                    String gstRtmpEdge =  DalbitUtil.getProperty("wowza.rtmp.edge") + "/" + streamName + "_aac";
                    String gstWebRtcUrl = DalbitUtil.getProperty("wowza.wss.url") ;
                    String gstWebRtcAppName = "edge";
                    String gstWebRtcStreamName = streamName + "_opus";
                    guestInfoVo.setRtmpOrigin(gstRtmpOrigin);
                    guestInfoVo.setRtmpEdge(gstRtmpEdge);
                    guestInfoVo.setWebRtcUrl(gstWebRtcUrl);
                    guestInfoVo.setWebRtcAppName(gstWebRtcAppName);
                    guestInfoVo.setWebRtcStreamName(gstWebRtcStreamName);
                    status = Status.공백;

                }else if("10".equals(mode)){    //게스트 통화중
                    P_RoomGuestVo apiData = new P_RoomGuestVo(djMemNo, memNo, roomNo, "", "", "", "", "", "", request);
                    status =  guestService.callBroadCastRoomGuestCancel(apiData, request);
                }

                //소켓통신
                if(status == null || "success".equals(status.getResult())){
                    try{
                        if(isGuest){
                            socketService.sendGuest(memNo, roomNo, djMemNo, mode, request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        }else{
                            //게스트 없을경우 연결종료
                            socketService.sendGuest(memNo, roomNo, djMemNo, "6", request, DalbitUtil.getAuthToken(request), guestInfoVo);
                        }

                    }catch(Exception e){}
                }
            }else{
                status = Status.파라미터오류;
            }
        }else{
            status = Status.파라미터오류;
        }
        return gsonUtil.toJson(new JsonOutputVo(status, guestInfoVo));
    }


    /**
     * 게스트신청
     */
    public String callGuestPropose(P_GuestProposeVo pGuestProposeVo, String memNo, String roomNo, String djMemNo, String mode, String nickname, HttpServletRequest request, String authToken) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestProposeVo);
        guestDao.callGuestPropose(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        DalbitUtil.getIntMap(resultMap, "propose_count");

        GuestInfoVo guestInfoVo = new GuestInfoVo();
        guestInfoVo.setMode(Integer.parseInt(mode));
        guestInfoVo.setMemNo(memNo);
        guestInfoVo.setNickNm(nickname);
        guestInfoVo.setProposeCnt(DalbitUtil.getIntMap(resultMap, "propose_count"));

        if(procedureVo.getRet().equals(Status.게스트신청_성공.getMessageCode())) {
            try{
                socketService.sendGuest(memNo, roomNo, djMemNo, mode, request, authToken, guestInfoVo);
            }catch(Exception e){}

            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_성공, guestInfoVo));
        }else if(procedureVo.getRet().equals(Status.게스트신청_회원아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트신청_방번호없음.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_방번호없음));
        }else if(procedureVo.getRet().equals(Status.게스트신청_종료된방번호.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_종료된방번호));
        }else if(procedureVo.getRet().equals(Status.게스트신청_청취자아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_청취자아님));
        }else if(procedureVo.getRet().equals(Status.게스트신청_신청불가상태.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_신청불가상태));
        }else if(procedureVo.getRet().equals(Status.게스트신청_이미신청중.getMessageCode())) {
            if(DalbitUtil.getIntMap(resultMap, "proposeState") == 2){
                guestInfoVo.setMode(1);
                return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_초대받은상태, guestInfoVo));
            }else{
                return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_이미신청중, guestInfoVo));
            }
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청_실패));
        }
    }


    /**
     * 게스트신청 취소
     */
    public String callGuestProposeCancel(P_GuestProposeVo pGuestProposeVo, String memNo, String roomNo, String djMemNo, String mode, String nickname, HttpServletRequest request, String authToken) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestProposeVo);
        guestDao.callGuestProposeCancel(procedureVo);

        HashMap resultMap = new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        GuestInfoVo guestInfoVo = new GuestInfoVo();
        guestInfoVo.setMode(Integer.parseInt(mode));
        guestInfoVo.setMemNo(memNo);
        guestInfoVo.setNickNm(nickname);
        guestInfoVo.setProposeCnt(DalbitUtil.getIntMap(resultMap, "propose_count"));

        String result;
        if(procedureVo.getRet().equals(Status.게스트신청취소_성공.getMessageCode())) {
            try{
                socketService.sendGuest(memNo, roomNo, djMemNo, mode, request, authToken, guestInfoVo);
            }catch(Exception e){}

            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_성공, guestInfoVo));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_회원아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_회원아님));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_방번호없음.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_방번호없음));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_종료된방번호.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_종료된방번호));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_청취자아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_청취자아님));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_불가상태.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_불가상태));
        }else if(procedureVo.getRet().equals(Status.게스트신청취소_신청상태아님.getMessageCode())) {
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_신청상태아님));
        }else{
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트신청취소_실패));
        }
    }


    /**
     * 게스트 관리
     */
    public String callGuestManagementList(P_GuestManagementListVo pGuestManagementListVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestManagementListVo);
        List<P_GuestManagementListVo> guestManagementVoList = guestDao.callGuestManagementList(procedureVo);

        HashMap guestVoList = new HashMap();
        if(DalbitUtil.isEmpty(guestManagementVoList)){
            ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo);
            DeviceVo deviceVo = new DeviceVo(request);
            guestVoList.put("list", new ArrayList<>());
            guestVoList.put("gstCnt", 0);
            guestVoList.put("proposeCnt", 0);
            guestVoList.put("totalCnt", 0);
            if(deviceVo.getOs() == 2){
                guestVoList.put("paging", new PagingVo(Integer.valueOf(procedureOutputVo.getRet()), pGuestManagementListVo.getPageNo(), pGuestManagementListVo.getPageCnt()));
            }else{
                guestVoList.put("paging", new PagingVo(0, pGuestManagementListVo.getPageNo(), pGuestManagementListVo.getPageCnt()));
            }
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트리스트_없음, guestVoList));
        }

        List<GuestListOutVo> outVoList = new ArrayList<>();
        for (int i=0; i<guestManagementVoList.size(); i++){
            outVoList.add(new GuestListOutVo(guestManagementVoList.get(i)));
        }
        ProcedureOutputVo procedureOutputVo = new ProcedureOutputVo(procedureVo, outVoList);
        HashMap resultMap = new Gson().fromJson(procedureOutputVo.getExt(), HashMap.class);

        guestVoList.put("gstCnt", DalbitUtil.getIntMap(resultMap, "guest_count"));
        guestVoList.put("proposeCnt", DalbitUtil.getIntMap(resultMap, "propose_count"));
        guestVoList.put("list", procedureOutputVo.getOutputBox());
        guestVoList.put("paging", new PagingVo(DalbitUtil.getIntMap(resultMap, "totalCnt"), DalbitUtil.getIntMap(resultMap, "pageNo"), DalbitUtil.getIntMap(resultMap, "pageCnt")));

        String result;
        if(Integer.parseInt(procedureVo.getRet()) > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_성공, guestVoList));
        } else if(Status.게스트관리조회_요청회원번호_회원아님.getMessageCode().equals(procedureVo.getRet())) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_요청회원번호_회원아님));
        } else if(Status.게스트관리조회_방번호없음.getMessageCode().equals((procedureVo.getRet()))) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_방번호없음));
        } else if(Status.게스트관리조회_종료된방번호.getMessageCode().equals((procedureVo.getRet()))) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_종료된방번호));
        } else if(Status.게스트관리조회_청취자아님.getMessageCode().equals((procedureVo.getRet()))) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_청취자아님));
        } else if(Status.게스트관리조회_권한없음.getMessageCode().equals((procedureVo.getRet()))) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_권한없음));
        } else {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트관리조회_실패));
        }

        return result;
    }


    /**
     * 게스트 초대
     */
    public Status callGuestInvite(P_GuestInviteVo pGuestInviteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestInviteVo);
        guestDao.callGuestInvite(procedureVo);

        Status status = null;
        if(procedureVo.getRet().equals(Status.게스트초대_성공.getMessageCode())) {
            status = Status.게스트초대_성공;
        }else if(procedureVo.getRet().equals(Status.게스트초대_회원아님.getMessageCode())) {
            status = Status.게스트초대_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트초대_초대대상_회원아님.getMessageCode())) {
            status = Status.게스트초대_초대대상_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트초대_방번호없음.getMessageCode())) {
            status = Status.게스트초대_방번호없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대_종료된방번호.getMessageCode())) {
            status = Status.게스트초대_종료된방번호;
        }else if(procedureVo.getRet().equals(Status.게스트초대_요청회원_방에없음.getMessageCode())) {
            status = Status.게스트초대_요청회원_방에없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대_권한없음.getMessageCode())) {
            status = Status.게스트초대_권한없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대_초대회원_방에없음.getMessageCode())) {
            status = Status.게스트초대_초대회원_방에없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대_초대불가.getMessageCode())) {
            status = Status.게스트초대_초대불가;
        }else if(procedureVo.getRet().equals(Status.게스트초대_이미초대중.getMessageCode())) {
            status = Status.게스트초대_이미초대중;
        }else if(procedureVo.getRet().equals(Status.게스트초대_이미존재_제한.getMessageCode())) {
            status = Status.게스트초대_이미존재_제한;
        }else if(procedureVo.getRet().equals(Status.게스트초대_다른회원_초대중.getMessageCode())) {
            status = Status.게스트초대_다른회원_초대중;
        }else{
            status = Status.게스트초대_실패;
        }
        return status;
    }


    /**
     * 게스트 초대 취소
     */
    public Status callGuestInviteCancel(P_GuestInviteVo pGuestInviteVo) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestInviteVo);
        guestDao.callGuestInviteCancel(procedureVo);

        Status status = null;
        if(procedureVo.getRet().equals(Status.게스트초대취소_성공.getMessageCode())) {
            status = Status.게스트초대취소_성공;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_회원아님.getMessageCode())) {
            status = Status.게스트초대취소_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_초대대상_회원아님.getMessageCode())) {
            status = Status.게스트초대취소_초대대상_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_방번호없음.getMessageCode())) {
            status = Status.게스트초대취소_방번호없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_종료된방번호.getMessageCode())) {
            status = Status.게스트초대취소_종료된방번호;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_요청회원_방에없음.getMessageCode())) {
            status = Status.게스트초대취소_요청회원_방에없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_권한없음.getMessageCode())) {
            status = Status.게스트초대취소_권한없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_초대회원_방에없음.getMessageCode())) {
            status = Status.게스트초대취소_초대회원_방에없음;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_초대취소불가.getMessageCode())) {
            status = Status.게스트초대취소_초대취소불가;
        }else if(procedureVo.getRet().equals(Status.게스트초대취소_초대중아님.getMessageCode())) {
            status = Status.게스트초대취소_초대중아님;
        }else{
            status = Status.게스트초대취소_실패;
        }

        return status;
    }


    /**
     * 게스트 초대 수락
     */
    public Status callGuestInviteOk(P_GuestInviteOkVo pGuestInviteOkVo) {
        ProcedureVo procedureVo = new ProcedureVo(pGuestInviteOkVo);
        guestDao.callGuestInviteOk(procedureVo);

        Status status = null;
        if(pGuestInviteOkVo.getYes_no() == 1) {
            if(procedureVo.getRet().equals(Status.게스트초대수락_성공.getMessageCode())) {
                status = Status.게스트초대수락_성공;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_회원아님.getMessageCode())) {
                status = Status.게스트초대수락_회원아님;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_방번호없음.getMessageCode())) {
                status = Status.게스트초대수락_방번호없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_종료된방번호.getMessageCode())) {
                status = Status.게스트초대수락_종료된방번호;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_요청회원_방에없음.getMessageCode())) {
                status = Status.게스트초대수락_요청회원_방에없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_불가.getMessageCode())) {
                status = Status.게스트초대수락_불가;
            }else if(procedureVo.getRet().equals(Status.게스트초대수락_초대상태아님.getMessageCode())) {
                status = Status.게스트초대수락_초대상태아님;
            }else{
                status = Status.게스트초대수락_실패;
            }
        } else {
            if(procedureVo.getRet().equals(Status.게스트초대거절_성공.getMessageCode())) {
                status = Status.게스트초대거절_성공;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_회원아님.getMessageCode())) {
                status = Status.게스트초대거절_회원아님;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_초대대상_회원아님.getMessageCode())) {
                status = Status.게스트초대거절_초대대상_회원아님;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_방번호없음.getMessageCode())) {
                status = Status.게스트초대거절_방번호없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_종료된방번호.getMessageCode())) {
                status = Status.게스트초대거절_종료된방번호;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_요청회원_방에없음.getMessageCode())) {
                status = Status.게스트초대거절_요청회원_방에없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_권한없음.getMessageCode())) {
                status = Status.게스트초대거절_권한없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_초대회원_방에없음.getMessageCode())) {
                status = Status.게스트초대거절_초대회원_방에없음;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_초대취소불가.getMessageCode())) {
                status = Status.게스트초대거절_초대취소불가;
            }else if(procedureVo.getRet().equals(Status.게스트초대거절_초대중아님.getMessageCode())) {
                status = Status.게스트초대거절_초대중아님;
            }else{
                status = Status.게스트초대거절_실패;
            }
        }
        return status;
    }


    /**
     * 게스트 지정하기
     */
    public Status callBroadCastRoomGuestAdd(P_RoomGuestVo pRoomGuestVo, HttpServletRequest request) {

        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestVo);
        guestDao.callBroadCastRoomGuestAdd(procedureVo);

        Status status = null;
        if(procedureVo.getRet().equals(Status.게스트지정.getMessageCode())){
            status = Status.게스트지정;
        }else if(procedureVo.getRet().equals(Status.게스트지정_회원아님.getMessageCode())) {
            status = Status.게스트지정_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트지정_해당방이없음.getMessageCode())) {
            status = Status.게스트지정_해당방이없음;
        }else if(procedureVo.getRet().equals(Status.게스트지정_방이종료되었음.getMessageCode())) {
            status = Status.게스트지정_방이종료되었음;
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아님.getMessageCode())) {
            status = Status.게스트지정_방소속_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트지정_방장아님.getMessageCode())) {
            status = Status.게스트지정_방장아님;
        }else if(procedureVo.getRet().equals(Status.게스트지정_방소속_회원아이디아님.getMessageCode())) {
            status = Status.게스트지정_방소속_회원아이디아님;
        }else if(procedureVo.getRet().equals(Status.게스트지정_불가.getMessageCode())) {
            status = Status.게스트지정_불가;
        }else if(procedureVo.getRet().equals(Status.게스트지정_초대수락상태아님.getMessageCode())) {
            status = Status.게스트지정_초대수락상태아님;
        }else{
            status = Status.게스트지정_실패;
        }

        return status;
    }


    /**
     * 게스트 취소
     */
    public Status callBroadCastRoomGuestCancel(P_RoomGuestVo pRoomGuestVo, HttpServletRequest request) {
        ProcedureVo procedureVo = new ProcedureVo(pRoomGuestVo);
        guestDao.callBroadCastRoomGuestCancel(procedureVo);

        log.info("프로시저 응답 코드: {}", procedureVo.getRet());
        log.info("프로시저 응답 데이타: {}", procedureVo.getExt());
        log.info(" ### 프로시저 호출결과 ###");

        Status status = null;
        if(procedureVo.getRet().equals(Status.게스트취소.getMessageCode())){
            status = Status.게스트취소;
        }else if(procedureVo.getRet().equals(Status.게스트취소_회원아님.getMessageCode())) {
            status = Status.게스트취소_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트취소_해당방이없음.getMessageCode())) {
            status = Status.게스트취소_해당방이없음;
        }else if(procedureVo.getRet().equals(Status.게스트취소_방이종료되었음.getMessageCode())) {
            status = Status.게스트취소_방이종료되었음;
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아님.getMessageCode())) {
            status = Status.게스트취소_방소속_회원아님;
        }else if(procedureVo.getRet().equals(Status.게스트취소_방장아님.getMessageCode())) {
            status = Status.게스트취소_방장아님;
        }else if(procedureVo.getRet().equals(Status.게스트취소_방소속_회원아이디아님.getMessageCode())) {
            status = Status.게스트취소_방소속_회원아이디아님;
        }else if(procedureVo.getRet().equals(Status.게스트취소_불가.getMessageCode())) {
            status = Status.게스트취소_불가;
        }else{
            status = Status.게스트취소_실패;
        }
        return status;
    }


    /**
     * 게스트 리스트 정보
     */
    public String selectGuestList(GuestListVo guestListVo, HttpServletRequest request) {
        List<P_GuestListVo> pGuestListVo = guestDao.selectGuestList(guestListVo);

        HashMap guestList = new HashMap();
        if(DalbitUtil.isEmpty(pGuestListVo)){
            guestList.put("list", new ArrayList<>());
            return gsonUtil.toJson(new JsonOutputVo(Status.게스트리스트조회_없음, guestList));
        }

        List<RoomGuestListOutVo> outVoList = new ArrayList<>();

        for (int i=0; i<pGuestListVo.size(); i++){
            RoomGuestListOutVo roomGuestListOut = new RoomGuestListOutVo(pGuestListVo.get(i), MemberVo.getMyMemNo(request), WOWZA_PREFIX);
            outVoList.add(roomGuestListOut);
        }
        guestList.put("list", outVoList);

        String result;
        if (pGuestListVo.size() > 0) {
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트리스트조회_성공, guestList));
        }else{
            result = gsonUtil.toJson(new JsonOutputVo(Status.게스트리스트조회_실패));
        }
        return result;
    }

    /**
     * 게스트 상태조회
     */
    public List<P_GuestListVo> getGuestInfo(P_GuestListVo pGuestListVo) {
        return guestDao.getGuestInfo(pGuestListVo);
    }
}

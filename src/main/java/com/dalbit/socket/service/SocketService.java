package com.dalbit.socket.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.GuestInfoVo;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.common.dao.CommonDao;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.ItemDetailVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.ProfileDao;
import com.dalbit.member.vo.procedure.P_LevelUpCheckVo;
import com.dalbit.socket.dao.SocketDao;
import com.dalbit.socket.vo.P_RoomMemberInfoSelectVo;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class SocketService {
    @Value("${socket.ip}")
    private String SERVER_SOCKET_IP;

    @Value("${socket.port}")
    private String SERVER_SOCKET_PORT;

    @Value("${socket.send.url}")
    private String SERVER_SOCKET_URL;

    @Value("${socket.global.room}")
    private String SERVER_SOCKET_GLOBAL_ROOM;

    @Autowired
    SocketDao socketDao;

    @Autowired
    RoomDao roomDao;

    @Autowired
    CommonService commonService;

    @Autowired
    CommonDao commonDao;

    @Autowired
    ProfileDao profileDao;

    public void sendSocketApi(String authToken, String roomNo, String params) {
        HttpURLConnection con = null;
        URL url = null;
        String result = "";

        params = StringUtils.defaultIfEmpty(params, "").trim();
        String request_uri = "https://" + SERVER_SOCKET_IP + ":" + SERVER_SOCKET_PORT + SERVER_SOCKET_URL + roomNo;

        log.info("소켓 request_uri: {}", request_uri);
        //log.error("Socket Start {}, {}, {}", roomNo, params);

        try{
            url = new URL(request_uri);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("x-custom-header", authToken);
            if(!"".equals(params)){
                //con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.setUseCaches(false);
                con.setAllowUserInteraction(true);

                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.write(params.getBytes("UTF-8"));
                out.flush();
            }

            result = readStream(con.getInputStream());
        }catch(IOException e){
            try {
                P_ErrorLogVo apiData = new P_ErrorLogVo();
                apiData.setOs("API");
                apiData.setDtype("SOCKET");
                apiData.setCtype(roomNo);
                String desc = "";
                if (!DalbitUtil.isEmpty(params)) {
                    desc = "Data : \n" + params + "\n";
                }
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                if (sw != null) {
                    desc += "Exception : \n" + sw.toString();
                }
                apiData.setDesc(desc);
                commonService.saveErrorLog(apiData);
                //log.info("Socket Rest {} error {}", request_uri, e.getMessage());
            }catch(Exception e1){}
            if(con != null){
                result = readStream(con.getErrorStream());
            }
        }

        if(!DalbitUtil.isEmpty(params) && params.indexOf("levelUp") > -1){
            log.error("Socket Result {}, {}, {}", roomNo, params, result);
        }else{
            log.info("Socket Result {}, {}, {}", roomNo, params, result);
        }
        //return new Gson().fromJson(result, Map.class);
    }

    /**
     * http 연결 읽기
     *
     * @param stream : 연결 스트림
     * @return
     * @throws GlobalException
     */
    private String readStream(InputStream stream){
        StringBuffer pageContents = new StringBuffer();
        try {
            InputStreamReader reader = new InputStreamReader(stream, "UTF-8");
            BufferedReader buff = new BufferedReader(reader);

            String pageContentsR = "";
            while((pageContentsR = buff.readLine()) != null){
                pageContents.append(pageContentsR);
                pageContents.append("\r\n");
            }
            reader.close();
            buff.close();

        }catch (Exception e){ }

        return pageContents.toString();
    }

    @Async("threadTaskExecutor")
    public void sendMessage(String memNo, String roomNo, String message, String authToken, boolean isLogin){
        log.info("Socket Start : sendMessage {}, {}, {}, {}", roomNo, memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(message) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            log.debug("send vo : " + vo.toString());
            if(vo.getMemNo() != null) {
                vo.setCommand("chat");
                vo.setMessage(message);

                log.debug("send message : " + vo.toString());
                sendSocketApi(authToken, roomNo, vo.toQueryString());
                //log.debug("send result : " + result.toString());

                //return result;
            }
        }

        //return null;
    }

    @Async("threadTaskExecutor")
    public void sendMessage(String message) {
        log.info("Socket Start : sendMessage {}", message);
        P_RoomListVo pRoomListVo = new P_RoomListVo();
        pRoomListVo.setMem_no("10000000000000");
        pRoomListVo.setPageNo(1);
        pRoomListVo.setPageCnt(100);
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);
        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);
        String result = "error";
        if(DalbitUtil.isEmpty(roomVoList)){
            result = "broadcast is nothing";
        }else{
            for(int i = 0; i < roomVoList.size(); i++){
                if(roomVoList.get(i).getState() != 4){
                    sendMessage(roomVoList.get(i), message);
                }
            }

        }
        //return result;
    }

    @Async("threadTaskExecutor")
    public void sendMessage(String message, List<String> listTargetRooms) {
        log.info("Socket Start : sendMessage {}", message);
        P_RoomListVo pRoomListVo = new P_RoomListVo();
        pRoomListVo.setMem_no("10000000000000");
        pRoomListVo.setPageNo(1);
        pRoomListVo.setPageCnt(100);
        ProcedureVo procedureVo = new ProcedureVo(pRoomListVo);
        List<P_RoomListVo> roomVoList = roomDao.callBroadCastRoomList(procedureVo);
        String result = "error";
        if(DalbitUtil.isEmpty(listTargetRooms)){
            result = "broadcast is nothing";
        }else{
            for(int i = 0; i < roomVoList.size(); i++){
                if(roomVoList.get(i).getState() != 4){
                    if(listTargetRooms.contains(roomVoList.get(i).getRoomNo())){
                        sendMessage(roomVoList.get(i), message);
                    }
                }
            }

        }
        //return result;
    }

    @Async("threadTaskExecutor")
    public void sendMessage(P_RoomListVo roomListVo, String message) {
        SocketVo vo = new SocketVo();
        vo.setMemNo(roomListVo.getBj_mem_no());
        vo.setLogin(1);
        vo.setCommand("reqBcStart");
        vo.setMessage(message);
        vo.setCtrlRole("0");
        vo.setRecvType("system");
        vo.setAuth(3);
        vo.setAuthName("운영자");
        sendSocketApi(roomListVo.getBj_mem_no(), roomListVo.getRoomNo(), vo.toQueryString());
    }

    @Async("threadTaskExecutor")
    public void changeRoomState(String roomNo, String memNo, int old_state, int state, String authToken, boolean isLogin){
        changeRoomState(roomNo, memNo, old_state, state, authToken, isLogin, null, null);
    }

    @Async("threadTaskExecutor")
    public void changeRoomState(String roomNo, String memNo, int old_state, int state, String authToken, boolean isLogin, SocketVo vo, String isAnt){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}, {}", roomNo, memNo, old_state, state, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        isAnt = isAnt == null ? "" : isAnt.trim().toUpperCase();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null){
                vo = getSocketVo(roomNo, memNo, isLogin);
            }
            if(vo.getMemNo() != null){
                String command = "reqMicOn";
                if (state == 0) {
                    //command = "reqMediaOff";
                    bjAntDisConnect(roomNo, memNo, authToken, isLogin, vo);
                    return;
                } else if (state == 3) { //통화중
                    if ((old_state == 0 || old_state == 6) && ("1".equals(isAnt) || "TRUE".equals(isAnt))) {
                        bjAntConnect(roomNo, memNo, authToken, isLogin, vo);
                    }
                    command = "reqCalling";
                } else if (state == 2) { // 마이크 오프
                    if ((old_state == 0 || old_state == 6) && ("1".equals(isAnt) || "TRUE".equals(isAnt))) {
                        bjAntConnect(roomNo, memNo, authToken, isLogin, vo);
                    }
                    command = "reqMicOff";
                } else {
                    if (old_state == 0 || old_state == 6) {
                        bjAntConnect(roomNo, memNo, authToken, isLogin, vo);
                        //command = "reqMediaOn";
                        return;
                    } else if (old_state == 3) {
                        command = "reqEndCall";
                    }
                }

                vo.setCommand(command);
                vo.setMessage(vo.getAuth() + "");
                vo.setRecvPosition("top1");
                vo.setRecvLevel(3);
                vo.setRecvType("system");

                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void changeRoomState(String roomNo, String memNo, int old_state, String ant, String call, String mic, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}, {}, {}", roomNo, memNo, ant, call, mic, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)) {
            if (vo == null) {
                vo = getSocketVo(roomNo, memNo, isLogin);
            }
            if (vo.getMemNo() != null) {
                if("0".equals(ant) || "FALSE".equals(ant.toUpperCase())) {
                    bjAntDisConnect(roomNo, memNo, authToken, isLogin, vo);
                }else{
                    if(old_state == 0 || old_state == 3){
                        bjAntConnect(roomNo, memNo, authToken, isLogin, vo);
                    }
                }
                if("0".equals(mic) || "FALSE".equals(mic.toUpperCase())) {
                    vo.setCommand("reqMicOff");
                    vo.setMessage(vo.getAuth() + "");
                    vo.setRecvPosition("top1");
                    vo.setRecvLevel(3);
                    vo.setRecvType("system");

                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }else{
                    vo.setCommand("reqMicOn");
                    vo.setMessage(vo.getAuth() + "");
                    vo.setRecvPosition("top1");
                    vo.setRecvLevel(3);
                    vo.setRecvType("system");

                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }
                if("0".equals(call) || "FALSE".equals(call.toUpperCase())) {
                    vo.setCommand("reqEndCall");
                    vo.setMessage(vo.getAuth() + "");
                    vo.setRecvPosition("top1");
                    vo.setRecvLevel(3);
                    vo.setRecvType("system");

                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }else{
                    vo.setCommand("reqCalling");
                    vo.setMessage(vo.getAuth() + "");
                    vo.setRecvPosition("top1");
                    vo.setRecvLevel(3);
                    vo.setRecvType("system");

                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }
            }
        }
    }

    @Async("threadTaskExecutor")
    public void bjAntConnect(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : bjAntConnect {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqBjAntConnect");
                //vo.setRecvDj(0);
                vo.setMessage("");
                //vo.setMessage("" + new java.util.Date().getTime());
                sendSocketApi(authToken, roomNo, vo.toQueryString());
                //return bjAntDisConnect(roomNo, authToken, vo);
            }
        }
    }

    @Async("threadTaskExecutor")
    public void bjAntDisConnect(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : bjAntDisConnect {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqBjAntDisconnect");
                vo.setMessage("DJ의 방송상태가 원활하지 않습니다.");
                //vo.setMessage("" + new java.util.Date().getTime());
                vo.setRecvDj(0);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
                bjAntDisConnect(roomNo, authToken, vo);
            }
        }
    }

    @Async("threadTaskExecutor")
    public void bjAntDisConnect(String roomNo, String authToken, SocketVo vo){
        vo.setCommand("chat");
        vo.setRecvType("system");
        vo.setRecvPosition("top1");
        vo.setRecvDj(1);
        vo.setRecvListener(0);
        vo.setRecvManager(0);
        vo.setRecvCommand("reqBjAntDisconnect");
        sendSocketApi(authToken, roomNo, vo.toQueryString());
    }

    @Async("threadTaskExecutor")
    public void changeRoomState(String roomNo, String memNo, int state, String authToken, boolean isLogin) {
        changeRoomState(roomNo, memNo, state, authToken, isLogin, null, null);
    }

    @Async("threadTaskExecutor")
    public void changeRoomState(String roomNo, String memNo, int state, String authToken, boolean isLogin, String type, SocketVo vo){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}", roomNo, memNo, state, isLogin);

        if("join".equals(type)){
            try{
                Thread.sleep(500);
            }catch(InterruptedException e){}
        }

        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null){
                vo = getSocketVo(roomNo, memNo, isLogin);
            }
            if(vo.getMemNo() != null) {
                String command = "reqMicOn";
                if (state == 3) { //통화중
                    command = "reqCalling";
                } else if (state == 2) { // 마이크 오프
                    command = "reqMicOff";
                } else if (state == 0) { // 미디어 오프
                    bjAntDisConnect(roomNo, memNo, authToken, isLogin, vo);
                }
                vo.setCommand(command);
                vo.setMessage(vo.getAuth() + "");
                vo.setRecvPosition("top1");
                vo.setRecvLevel(3);
                vo.setRecvType("system");
                vo.setRecvMemNo(memNo);

                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void changeManager(String roomNo, String memNo, String menagerMemNo, boolean isManager, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeManager {}, {}, {}, {}, {}", roomNo, memNo, menagerMemNo, isManager, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(menagerMemNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqGrant");
                vo.setMessage(isManager ? "1" : "0");
                vo.setRecvMemNo(menagerMemNo);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void changeRoomInfo(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeManager {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqRoomChangeInfo");
                vo.setMessage(roomInfo);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void changeCount(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeCount {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqChangeCount");
                vo.setMessage(roomInfo);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void sendLike(String roomNo, String memNo, boolean isFirst, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendLike {}, {}, {}, {}", roomNo, memNo, isFirst, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(isFirst){
                SocketVo vo1 = vo;
                if(vo1 != null || vo1.getFan() == 0){
                    vo1.setCommand("reqGoodFirst");
                    vo1.setRecvMemNo(memNo);
                    vo1.setMessage("{\"time\" : 0}");
                    sendSocketApi(authToken, roomNo, vo1.toQueryString());
                }
            }
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqGood");
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void sendBooster(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendBooster {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = memNo == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqBooster");
                //vo.setMessage(itemMap);
                vo.setRecvTime(3);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void sendNotice(String roomNo, String memNo, String notice, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendNotice {}, {}, {}, {}", roomNo, memNo, notice, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        notice = notice == null ? "" : notice.trim();
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqNotice");
                vo.setRecvTime(3);
                vo.setRecvDj(0);
                vo.setMessage(notice);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void sendStory(String roomNo, String memNo, Object story, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendStory {}, {}, {}, {}", roomNo, memNo, story, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && story != null){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqStory");
                vo.setRecvTime(3);
                vo.setRecvListener(0);
                vo.setRecvManager(0);
                vo.setMessage(story);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void addFan(String roomNo, String memNo, String recvMemNo, String authToken, String fan, boolean isLogin, SocketVo vo){
        log.info("Socket Start : addFan {}, {}, {}, {}, {}", roomNo, memNo, recvMemNo, fan, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqFan");
                vo.setMessage(fan);
                vo.setRecvMemNo(recvMemNo);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void giftDal(String memNo, String giftedMemNo, int dal, String authToken, boolean isLogin){
        log.info("Socket Start : giftDal {}, {}, {}, {}", memNo, giftedMemNo, dal, isLogin);
        memNo = memNo == null ? "" : memNo.trim();
        giftedMemNo = giftedMemNo == null ? "" : giftedMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(giftedMemNo) && !"".equals(authToken) && dal > 0){
            HashMap myInfo = getMyInfo(memNo);
            if(myInfo != null){
                SocketVo vo = new SocketVo(memNo, giftedMemNo, isLogin);
                vo.setMemNk(DalbitUtil.getStringMap(myInfo, "nickName"));
                vo.setCommand("reqGiftDal");
                HashMap socketMap = new HashMap();
                socketMap.put("itemCnt", dal);
                vo.setMessage(socketMap);
                sendSocketApi(authToken, SERVER_SOCKET_GLOBAL_ROOM, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void giftItem(String roomNo, String memNo, String giftedMemNo, Object item, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : giftItem {}, {}, {}, {}, {}", roomNo, memNo, giftedMemNo, item, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && item != null){
            if(vo != null || vo.getMemNo() != null) {
                vo.setCommand("reqGiftImg");
                vo.setMessage(item);
                if (giftedMemNo != null && !"".equals(giftedMemNo.trim())) {
                    vo.setRecvMemNo(giftedMemNo);
                }
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    /**
     * 게스트 노드 호출
     * @param roomNo 방번호
     * @param bjMemNo bj 회원번호
     * @param gstMemNo 게스트 회원번호
     * @param type 타입 (1:초대, 2:취소, 3:수락, 4:거절, 5:신청, 6:퇴장, 7:신청취소, 8:방송연결, 9:비정상종료)
     * @param authToken 토큰
     * @return
     */
    @Async("threadTaskExecutor")
    public void execGuest(String roomNo, String bjMemNo, String gstMemNo, int type, String authToken, boolean isLogin){
        log.info("Socket Start : execGuest {}, {}, {}, {}, {}", roomNo, bjMemNo, gstMemNo, type, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        bjMemNo = bjMemNo == null ? "" : bjMemNo.trim();
        gstMemNo = gstMemNo == null ? "" : gstMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(bjMemNo) && !"".equals(gstMemNo) && !"".equals(authToken) && type > 0 && type < 10){
            String recvMemNo = gstMemNo;
            String sendMemno = bjMemNo;
            if(type > 2){
                recvMemNo = bjMemNo;
                sendMemno = gstMemNo;
            }
            SocketVo vo = getSocketVo(roomNo, sendMemno, isLogin);
            if(vo.getMemNo() != null) {
                vo.setRecvMemNo(recvMemNo);
                vo.setMessage(type + "");
                vo.setCommand("reqGuest");
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void kickout(String roomNo, String memNo, String kickedMemNo, String authToken, boolean isLogin, SocketVo vo, HashMap bolckedMap){
        log.info("Socket Start : kickout {}, {}, {}, {}, {}", roomNo, memNo, kickedMemNo, isLogin, authToken);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        kickedMemNo = kickedMemNo == null ? "" : kickedMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(kickedMemNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqKickOut");
                if (bolckedMap != null) {
                    HashMap socketMap = new HashMap();
                    socketMap.put("sndAuth", vo.getAuth());
                    socketMap.put("sndMemNo", vo.getMemNo());
                    socketMap.put("sndMemNk", vo.getMemNk());
                    socketMap.put("revMemNo", kickedMemNo);
                    socketMap.put("revMemNk", DalbitUtil.getStringMap(bolckedMap, "nickName"));
                    vo.setMessage(socketMap);
                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }
            }
        }
    }

    @Async("threadTaskExecutor")
    public void banWord(String roomNo, String memNo, String authToken, boolean isLogin){
        log.info("Socket Start : banWord {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqBanWord");
                HashMap socketMap = new HashMap();
                socketMap.put("channel", roomNo);
                socketMap.put("memNo", vo.getMemNo());
                vo.setMessage(socketMap);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void chatEnd(String roomNo, String memNo, String authToken, int auth, boolean isLogin, SocketVo vo) {
        log.info("Socket Start : chatEnd {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setRecvMemNo(memNo);
                vo.setCommand("chatEnd");

                if(auth == 3 ){
                    HashMap data = new HashMap();
                    data.put("message", "roomOut");
                    vo.setRecvMemNo("roomOut");
                    vo.setMessage(data);
                }else{
                    HashMap data = new HashMap();
                    data.put("authToken", authToken);
                    data.put("memNo", memNo);
                    data.put("message", "userOut");
                    vo.setRecvMemNo(memNo);
                    vo.setMessage(data);
                }

                sendSocketApi(authToken, roomNo, vo.toQueryString());
                if(auth == 3 ){
                    HashMap data = new HashMap();
                    data.put("authToken", authToken);
                    data.put("memNo", memNo);
                    data.put("message", "roomOut");
                    vo.setRecvMemNo(memNo);
                    vo.setMessage(data);
                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }
            }
        }
    }

    @Async("threadTaskExecutor")
    public void changeMemberInfo(String memNo, Object message, String authToken, boolean isLogin) {
        log.info("Socket Start : changeMemberInfo {}, {}, {}", memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = new SocketVo();
            vo.setMemNo(memNo);
            vo.setLogin(isLogin ? 1 : 0);
            vo.setCommand("reqMyInfo");
            vo.setMessage(message);

            log.info("Socket vo to Query String: {}",vo.toQueryString());
            sendSocketApi(authToken, SERVER_SOCKET_GLOBAL_ROOM, vo.toQueryString());
        }
    }

    @Async("threadTaskExecutor")
    public void roomChangeTime(String roomNo, String memNo, String extendedTime, String authToken, boolean isLogin, SocketVo vo) {
        log.info("Socket Start : changeMemberInfo {}, {}, {}, {}, {}", roomNo, memNo, extendedTime, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        extendedTime = extendedTime == null ? "" : extendedTime.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(extendedTime) && !"".equals(authToken)){
            if(vo != null && vo.getMemNo() != null) {
                vo.setCommand("reqRoomChangeTime");
                vo.setMessage(extendedTime);
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    public SocketVo getSocketVo(String roomNo, String memNo, boolean isLogin){
        try{
            return new SocketVo(memNo, getUserInfo(roomNo, memNo, isLogin), isLogin);
        }catch(Exception e){}
        return null;
    }


    public HashMap getUserInfo(String roomNo, String memNo, boolean isLogin){
        P_RoomMemberInfoSelectVo pRoomMemberInfoVo = new P_RoomMemberInfoSelectVo();
        pRoomMemberInfoVo.setMem_no(memNo);
        pRoomMemberInfoVo.setRoom_no(roomNo);

        try{
            ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
            socketDao.callBroadcastMemberInfo(procedureVo);
            return new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        }catch(Exception e){}
        return null;
    }

    public HashMap getMyInfo(String memNo){
        P_RoomMemberInfoSelectVo apiData = new P_RoomMemberInfoSelectVo();
        apiData.setMem_no(memNo);

        try{
            ProcedureVo procedureVo = new ProcedureVo(apiData);
            socketDao.callBroadcastMemberInfo(procedureVo);
            return new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }

    @Async("threadTaskExecutor")
    public void sendDjLevelUp(String roomNo, HttpServletRequest request, SocketVo vo, String memNo, String authToken){
        HashMap itemMap = new HashMap();
        itemMap.put("itemNo", DalbitUtil.getProperty("item.code.levelUp"));
        ItemDetailVo item = commonDao.selectItem(DalbitUtil.getProperty("item.code.levelUp"));
        String itemNm = item.getItemNm();
        String itemThumbs = item.getThumbs();
        itemMap.put("itemNm", itemNm);
        itemMap.put("itemCnt", 1);
        itemMap.put("repeatCnt", 1);
        itemMap.put("itemImg", itemThumbs);
        itemMap.put("isSecret", false);
        itemMap.put("itemType", "levelUp");
        itemMap.put("authName", "");
        itemMap.put("auth", 0);
        itemMap.put("nickNm", "");
        itemMap.put("memNo", "");
        itemMap.put("dalCnt", 0);
        //String memNo = new MemberVo().getMyMemNo(request);
        //String authToken = DalbitUtil.getAuthToken(request);

        log.error("Socket Start : djLlevelUp {}, {}, {}, {}", roomNo, memNo, itemMap, vo);
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && itemMap != null){
            if(vo != null && vo.getMemNo() != null) {
                //vo.setCommand("reqLevelUpBj");
                vo.setCommand("reqGiftImg");
                vo.setMessage(itemMap);
                vo.setRecvDj(0);

                try{
                    Thread.sleep(5000);
                }catch(InterruptedException e){}
                sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void sendLevelUp(String memNo, String roomNo, HttpServletRequest request, String authToken){
        P_LevelUpCheckVo pLevelUpCheckVo = new P_LevelUpCheckVo();
        pLevelUpCheckVo.setMem_no(memNo);
        ProcedureVo procedureLevelCheckVo = new ProcedureVo(pLevelUpCheckVo);
        profileDao.callMemberLevelUpCheck(procedureLevelCheckVo);
        HashMap resultLevelUpCheckMap = new Gson().fromJson(procedureLevelCheckVo.getExt(), HashMap.class);
        SocketVo djVo = getSocketVo(roomNo, memNo, DalbitUtil.isLogin(request));

        int newLevel = (DalbitUtil.getIntMap(resultLevelUpCheckMap, "newLevel") - 1) / 10;
        String[] frameColor = DalbitUtil.getProperty("level.color." + newLevel).split(",");

        HashMap levelUp = new HashMap();
        levelUp.put("memNo", memNo);
        levelUp.put("nk", djVo.getMemNk());
        levelUp.put("level", DalbitUtil.getIntMap(resultLevelUpCheckMap, "newLevel"));
        levelUp.put("grade", resultLevelUpCheckMap.get("newGrade"));
        levelUp.put("image", djVo.getMemImg());
        levelUp.put("auth", djVo.getAuth());
        levelUp.put("dal", DalbitUtil.getIntMap(resultLevelUpCheckMap, "rewardDal"));
        levelUp.put("byul", DalbitUtil.getIntMap(resultLevelUpCheckMap, "rewardByeol"));
        levelUp.put("frameName", resultLevelUpCheckMap.get("newGrade") + " 프레임");
        levelUp.put("frameColor", frameColor);
        levelUp.put("frameImg", StringUtils.replace(DalbitUtil.getProperty("level.frame"),"[level]", resultLevelUpCheckMap.get("newLevel") + ""));

        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        log.info("Socket Start : levelUp {}, {}, {}, {}, {}, {}", roomNo, memNo, levelUp, authToken, djVo, (djVo == null ? null : djVo.getMemNo()));
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && levelUp != null){
            if(djVo != null && djVo.getMemNo() != null) {
                djVo.setCommand("reqLevelUpSelf");
                djVo.setMessage(levelUp);
                djVo.setRecvMemNo(memNo);
                sendSocketApi(authToken, roomNo, djVo.toQueryString());
            }
        }
    }

    @Async("threadTaskExecutor")
    public void chatEndRed(String memNo, String roomNo, HttpServletRequest request, String authToken){
        SocketVo vo = getSocketVo(roomNo, memNo, true);
        if(vo != null && vo.getMemNo() != null){
            vo.setCommand("chatEndRed");
            vo.setRecvMemNo("!" + memNo);
            vo.setMessage("다른 기기에서 로그인 되었습니다.");
            sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
    }

    @Async("threadTaskExecutor")
    public void sendGuest(String memNo, String roomNo, String mode, HttpServletRequest request, String authToken, GuestInfoVo guestInfoVo){
        sendGuest(memNo, roomNo, null, mode, request, authToken, guestInfoVo);
    }

    @Async("threadTaskExecutor")
    public void sendGuest(String memNo, String roomNo, String djMemNo, String mode, HttpServletRequest request, String authToken, GuestInfoVo guestInfoVo){
        log.info("Socket Start : reqGuest {}, {}, {}, {}, {}, {}", roomNo, memNo, djMemNo, mode, authToken, guestInfoVo.toString());

        SocketVo vo = getSocketVo(roomNo, memNo, DalbitUtil.isLogin(request));
        String nickNm = guestInfoVo.getNickNm();
        if(vo != null && vo.getMemNo() != null){
            vo.setCommand("reqGuest");
            HashMap guestMap = new HashMap();
            guestMap.put("memNo", memNo);
            guestMap.put("mode", Integer.parseInt(mode));
            guestMap.put("nickNm", nickNm);
            guestMap.put("profImg", guestInfoVo.getProfImg());
            guestMap.put("rtmpOrigin", guestInfoVo.getRtmpOrigin());
            guestMap.put("rtmpEdge", guestInfoVo.getRtmpEdge());
            guestMap.put("webRtcUrl", guestInfoVo.getWebRtcUrl());
            guestMap.put("webRtcAppName", guestInfoVo.getWebRtcAppName());
            guestMap.put("webRtcStreamName", guestInfoVo.getWebRtcStreamName());
            guestMap.put("proposeCnt", guestInfoVo.getProposeCnt());    //5:신청, 7:신청 취소일 경우 사용
            guestMap.put("msg", "");    //msg

            if("1".equals(mode)) { // 초대
                /*vo.setRecvDj(0);
                vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "게스트 초대 요청을 보냈습니다.");
                vo.setRecvMemNo(memNo);
            }else if("2".equals(mode)){ // 초대취소
                /*vo.setRecvDj(0);
                vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "DJ님이 게스트 초대를 취소하셨습니다.");
                vo.setRecvMemNo(memNo);
            }else if("3".equals(mode)){ // 수락
                /*vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "");
                vo.setRecvMemNo(djMemNo);
            }else if("4".equals(mode)){ // 거절
                /*vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "게스트 초대가 거절당했습니다.");
                vo.setRecvMemNo(djMemNo);
            }else if("5".equals(mode)){ // 신청
                /*vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "게스트 신청이 도착했습니다.");
                vo.setRecvMemNo(djMemNo);
            }else if("6".equals(mode) || "9".equals(mode) || "10".equals(mode)) { // 퇴장, 비정상종료, 게스트통화중
                if("6".equals(mode)){
                    guestMap.put("msg", nickNm+"님의 게스트 연결이 종료되었습니다.");
                }else if("9".equals(mode)){
                    guestMap.put("msg", nickNm+"님의 게스트 연결상태에 문제가 있어 연결이 종료되었습니다.");
                }else if("10".equals(mode)){
                    guestMap.put("msg", nickNm+"님의 게스트 통화중으로 연결이 종료되었습니다.");
                }
                guestMap.put("mode", 6);
                vo.setRecvMemNo(djMemNo);
            }else if("7".equals(mode)){ // 신청취소
                /*vo.setRecvManager(0);
                vo.setRecvListener(0);*/
                guestMap.put("msg", "게스트 참여 신청을 취소했습니다.");
                vo.setRecvMemNo(djMemNo);
            }else if("8".equals(mode)){ // 방송연결
                //vo.setRecvMemNo(djMemNo);
                guestMap.put("msg", nickNm+"님께서 게스트로 방송에 참여하였습니다.");
                vo.setNotMemNo(memNo);
            }
            vo.setMessage(guestMap);
            sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
    }

    @Async("threadTaskExecutor")
    public void changeRoomFreeze(String memNo, String roomNo, Object message, String authToken, boolean isLogin) {
        log.info("Socket Start : changeRoomFreeze {}, {}, {}", memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = new SocketVo();
            vo.setMemNo(memNo);
            vo.setLogin(isLogin ? 1 : 0);
            vo.setCommand("reqRoomLock");
            vo.setMessage(message);

            log.info("Socket vo to Query String: {}",vo.toQueryString());
            sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
    }

    @Async("threadTaskExecutor")
    public void sendRoomCreate(String memNo, String roomNo, Object message, String authToken, boolean isLogin, String memNoStr) {
        log.info("Socket Start : changeRoomFreeze {}, {}, {}", memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = new SocketVo();
            vo.setMemNo(memNo);
            vo.setLogin(isLogin ? 1 : 0);
            vo.setCommand("reqSocketPush");
            vo.setRecvMemNo(memNoStr);
            vo.setMessage(message);

            log.info("Socket vo to Query String: {}",vo.toQueryString());
            sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
    }


    /**
     * 보름달 체크
     */
    @Async("threadTaskExecutor")
    public void sendMoonCheck(String roomNo, HashMap message, String authToken, boolean isLogin, String callState) {
        if (DalbitUtil.getIntMap(message, "moonStep") == 4){
            log.error("Socket Start : sendMoonCheck {}, {}, {}", message, isLogin, callState);
        }else{
            log.info("Socket Start : sendMoonCheck {}, {}, {}", message, isLogin, callState);
        }

        authToken = authToken == null ? "" : authToken.trim();

        HashMap socketMap = new HashMap();
        socketMap.put("moonStep", DalbitUtil.getIntMap(message, "moonStep"));
        socketMap.put("moonStepFileNm", DalbitUtil.getStringMap(message, "moonStepFileNm"));
        socketMap.put("moonStepAniFileNm", DalbitUtil.getStringMap(message, "moonStepAniFileNm"));
        socketMap.put("dlgTitle", DalbitUtil.getStringMap(message, "dlgTitle"));
        socketMap.put("dlgText", DalbitUtil.getStringMap(message, "dlgText"));
        socketMap.put("aniDuration", DalbitUtil.getIntMap(message, "aniDuration"));

        HashMap moonCheckMap = new HashMap();
        moonCheckMap.put("clientCommand", "moonCheck");
        moonCheckMap.put("data", socketMap);

        SocketVo vo = new SocketVo();
        vo.setLogin(isLogin ? 1 : 0);
        vo.setCommand("reqByPass");
        vo.setMessage(moonCheckMap);

        if (DalbitUtil.getIntMap(message, "moonStep") == 4){
            log.error("Socket vo to Query String: {}",vo.toQueryString());
        }else{
            log.info("Socket vo to Query String: {}",vo.toQueryString());
        }

        if("gift".equals(callState) && DalbitUtil.getIntMap(message, "moonStep") == 4){
            try{
                Thread.sleep(5000);
            }catch(InterruptedException e){}

        }
        sendSocketApi(authToken, roomNo, vo.toQueryString());

    }
}


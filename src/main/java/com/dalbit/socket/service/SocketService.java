package com.dalbit.socket.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.procedure.P_RoomListVo;
import com.dalbit.broadcast.vo.request.RoomListVo;
import com.dalbit.common.service.CommonService;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.common.vo.procedure.P_ErrorLogVo;
import com.dalbit.exception.GlobalException;
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

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> sendSocketApi(String authToken, String roomNo, String params) {
        HttpURLConnection con = null;
        URL url = null;
        String result = "";

        params = StringUtils.defaultIfEmpty(params, "").trim();

        String request_uri = "https://" + SERVER_SOCKET_IP + ":" + SERVER_SOCKET_PORT + SERVER_SOCKET_URL + roomNo;

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

        log.info("Socket Result {}, {}, {}", roomNo, params, result);
        return new Gson().fromJson(result, Map.class);
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
    public Map<String, Object> sendMessage(String memNo, String roomNo, String message, String authToken, boolean isLogin){
        log.info("Socket Start : sendMessage {}, {}, {}, {}", roomNo, memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(message) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            log.debug("send vo : " + vo.toString());
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("chat");
            vo.setMessage(message);

            log.debug("send message : " + vo.toString());
            Map<String, Object> result = sendSocketApi(authToken, roomNo, vo.toQueryString());
            log.debug("send result : " + result.toString());

            return result;
        }

        return null;
    }

    @Async("threadTaskExecutor")
    public String sendMessage(String message) {
        log.info("Socket Start : sendMessage {}", message);
        RoomListVo pRoomListVo = new RoomListVo();
        pRoomListVo.setPage(1);
        pRoomListVo.setRecords(100);
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
        return result;
    }

    @Async("threadTaskExecutor")
    public String sendMessage(String message, List<String> listTargetRooms) {
        log.info("Socket Start : sendMessage {}", message);
        RoomListVo pRoomListVo = new RoomListVo();
        pRoomListVo.setPage(1);
        pRoomListVo.setRecords(100);
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
        return result;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendMessage(P_RoomListVo roomListVo, String message) {
        SocketVo vo = new SocketVo();
        vo.setMemNo(roomListVo.getBj_mem_no());
        vo.setLogin(1);
        vo.setCommand("reqBcStart");
        vo.setMessage(message);
        vo.setCtrlRole("0");
        vo.setRecvType("system");
        vo.setAuth(3);
        vo.setAuthName("운영자");
        return sendSocketApi(roomListVo.getBj_mem_no(), roomListVo.getRoomNo(), vo.toQueryString());
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int old_state, int state, String authToken, boolean isLogin){
        return changeRoomState(roomNo, memNo, old_state, state, authToken, isLogin, null);
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int old_state, int state, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}, {}", roomNo, memNo, old_state, state, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null){
                vo = getSocketVo(roomNo, memNo, isLogin);
            }
            if(vo.getMemNo() == null){
                return null;
            }

            String command = "reqMicOn";
            if(state == 0) {
                //command = "reqMediaOff";
                return bjAntDisConnect(roomNo, memNo, authToken, isLogin, vo);
            }else if(state == 3){ //통화중
                command = "reqCalling";
            }else if(state == 2){ // 마이크 오프
                command = "reqMicOff";
            }else{
                if(old_state == 0 || old_state == 6){
                    return bjAntConnect(roomNo, memNo, authToken, isLogin, vo);
                    //command = "reqMediaOn";
                }else if(old_state == 3){
                    command = "reqEndCall";
                }
            }

            vo.setCommand(command);
            vo.setMessage(vo.getAuth() + "");
            vo.setRecvPosition("top1");
            vo.setRecvLevel(3);
            vo.setRecvType("system");

            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }

        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> bjAntConnect(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : bjAntConnect {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqBjAntConnect");
            vo.setRecvDj(0);
            vo.setMessage("");
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
            //return bjAntDisConnect(roomNo, authToken, vo);
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> bjAntDisConnect(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : bjAntDisConnect {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqBjAntDisconnect");
            vo.setMessage("DJ의 방송상태가 원활하지 않습니다.");
            vo.setRecvDj(0);
            sendSocketApi(authToken, roomNo, vo.toQueryString());
            return bjAntDisConnect(roomNo, authToken, vo);
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> bjAntDisConnect(String roomNo, String authToken, SocketVo vo){
        vo.setCommand("chat");
        vo.setRecvType("system");
        vo.setRecvPosition("top1");
        vo.setRecvDj(1);
        return sendSocketApi(authToken, roomNo, vo.toQueryString());
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int state, String authToken, boolean isLogin) {
        return changeRoomState(roomNo, memNo, state, authToken, isLogin, null, null);
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int state, String authToken, boolean isLogin, String type, SocketVo vo){
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
            if(vo.getMemNo() == null){
                return null;
            }

            String command = "reqMicOn";
            if(state == 3){ //통화중
                command = "reqCalling";
            }else if(state == 2){ // 마이크 오프
                command = "reqMicOff";
            }else if(state == 0){ // 미디어 오프
                return bjAntDisConnect(roomNo, memNo, authToken, isLogin, vo);
            }
            vo.setCommand(command);
            vo.setMessage(vo.getAuth() + "");
            vo.setRecvPosition("top1");
            vo.setRecvLevel(3);
            vo.setRecvType("system");
            vo.setRecvMemNo(memNo);

            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }

        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeManager(String roomNo, String memNo, String menagerMemNo, boolean isManager, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeManager {}, {}, {}, {}, {}", roomNo, memNo, menagerMemNo, isManager, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(menagerMemNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqGrant");
            vo.setMessage(isManager ? "1" : "0");
            vo.setRecvMemNo(menagerMemNo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeRoomInfo(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeManager {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqRoomChangeInfo");
            vo.setMessage(roomInfo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeCount(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : changeCount {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqChangeCount");
            vo.setMessage(roomInfo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendLike(String roomNo, String memNo, boolean isFirst, String authToken, boolean isLogin, SocketVo vo){
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
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqGood");
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendBooster(String roomNo, String memNo, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendBooster {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = memNo == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqBooster");
            //vo.setMessage("{\"time\" : 1800}");
            vo.setRecvTime(3);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendNotice(String roomNo, String memNo, String notice, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendNotice {}, {}, {}, {}", roomNo, memNo, notice, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        notice = notice == null ? "" : notice.trim();
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqNotice");
            vo.setRecvTime(3);
            vo.setRecvDj(0);
            vo.setMessage(notice);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendStory(String roomNo, String memNo, Object story, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : sendStory {}, {}, {}, {}", roomNo, memNo, story, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && story != null){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqStory");
            vo.setRecvTime(3);
            vo.setRecvListener(0);
            vo.setRecvManager(0);
            vo.setMessage(story);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> addFan(String roomNo, String memNo, String recvMemNo, String authToken, String fan, boolean isLogin, SocketVo vo){
        log.info("Socket Start : addFan {}, {}, {}, {}, {}", roomNo, memNo, recvMemNo, fan, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqFan");
            vo.setMessage(fan);
            vo.setRecvMemNo(recvMemNo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> giftDal(String memNo, String giftedMemNo, int dal, String authToken, boolean isLogin){
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
                return sendSocketApi(authToken, SERVER_SOCKET_GLOBAL_ROOM, vo.toQueryString());
            }
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> giftItem(String roomNo, String memNo, String giftedMemNo, Object item, String authToken, boolean isLogin, SocketVo vo){
        log.info("Socket Start : giftItem {}, {}, {}, {}, {}", roomNo, memNo, giftedMemNo, item, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && item != null){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqGiftImg");
            vo.setMessage(item);
            if(giftedMemNo != null && !"".equals(giftedMemNo.trim())){
                vo.setRecvMemNo(giftedMemNo);
            }
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    /**
     * 게스트 노드 호출
     * @param roomNo 방번호
     * @param bjMemNo bj 회원번호
     * @param gstMemNo 게스트 회원번호
     * @param type 타입 (1:초대, 2:취소, 3:승락, 4:거절, 5:신청, 6:퇴장)
     * @param authToken 토큰
     * @return
     */
    @Async("threadTaskExecutor")
    public Map<String, Object> execGuest(String roomNo, String bjMemNo, String gstMemNo, int type, String authToken, boolean isLogin){
        log.info("Socket Start : execGuest {}, {}, {}, {}, {}", roomNo, bjMemNo, gstMemNo, type, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        bjMemNo = bjMemNo == null ? "" : bjMemNo.trim();
        gstMemNo = gstMemNo == null ? "" : gstMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(bjMemNo) && !"".equals(gstMemNo) && !"".equals(authToken) && type > 0 && type < 7){
            String recvMemNo = gstMemNo;
            String sendMemno = bjMemNo;
            if(type > 2){
                recvMemNo = bjMemNo;
                sendMemno = gstMemNo;
            }
            SocketVo vo = getSocketVo(roomNo, sendMemno, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setRecvMemNo(recvMemNo);
            vo.setMessage(type + "");
            vo.setCommand("reqGuest");
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> kickout(String roomNo, String memNo, String kickedMemNo, String authToken, boolean isLogin, SocketVo vo, HashMap bolckedMap){
        log.info("Socket Start : kickout {}, {}, {}, {}, {}", roomNo, memNo, kickedMemNo, isLogin, authToken);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        kickedMemNo = kickedMemNo == null ? "" : kickedMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(kickedMemNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }

            vo.setCommand("reqKickOut");
            if(bolckedMap != null){
                HashMap socketMap = new HashMap();
                socketMap.put("sndAuth", vo.getAuth());
                socketMap.put("sndMemNo", vo.getMemNo());
                socketMap.put("sndMemNk", vo.getMemNk());
                socketMap.put("revMemNo", kickedMemNo);
                socketMap.put("revMemNk", DalbitUtil.getStringMap(bolckedMap, "nickName"));
                vo.setMessage(socketMap);
                return sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> banWord(String roomNo, String memNo, String authToken, boolean isLogin){
        log.info("Socket Start : banWord {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqBanWord");
            HashMap socketMap = new HashMap();
            socketMap.put("channel", roomNo);
            socketMap.put("memNo", vo.getMemNo());
            vo.setMessage(socketMap);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> chatEnd(String roomNo, String memNo, String authToken, int auth, boolean isLogin, SocketVo vo) {
        log.info("Socket Start : chatEnd {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setRecvMemNo(memNo);
            vo.setCommand("chatEnd");

            HashMap data = new HashMap();
            data.put("message", auth == 3 ? "roomOut" : "userOut");
            data.put("authToken", authToken);
            data.put("memNo", memNo);
            vo.setMessage(data);

            log.debug(" @@@ chatEnd @@@");
            log.debug(vo.toQueryString());
            log.debug(" @@@ chatEnd @@@");
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeMemberInfo(String memNo, Object message, String authToken, boolean isLogin) {
        log.info("Socket Start : changeMemberInfo {}, {}, {}", memNo, message, isLogin);
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = new SocketVo();
            vo.setMemNo(memNo);
            vo.setLogin(isLogin ? 1 : 0);
            vo.setCommand("reqMyInfo");
            vo.setMessage(message);
            return sendSocketApi(authToken, SERVER_SOCKET_GLOBAL_ROOM, vo.toQueryString());
        }
        return null;
    }
    @Async("threadTaskExecutor")
    public Map<String, Object> roomChangeTime(String roomNo, String memNo, String extendedTime, String authToken, boolean isLogin, SocketVo vo) {
        log.info("Socket Start : changeMemberInfo {}, {}, {}, {}, {}", roomNo, memNo, extendedTime, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        extendedTime = extendedTime == null ? "" : extendedTime.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(extendedTime) && !"".equals(authToken)){
            if(vo == null || vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqRoomChangeTime");
            vo.setMessage(extendedTime);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
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
}


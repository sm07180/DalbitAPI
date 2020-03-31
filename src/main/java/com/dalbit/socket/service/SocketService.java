package com.dalbit.socket.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.RoomOutVo;
import com.dalbit.broadcast.vo.procedure.P_RoomInfoViewVo;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberInfoVo;
import com.dalbit.common.code.Status;
import com.dalbit.common.vo.ProcedureOutputVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.dao.MypageDao;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.procedure.P_MemberInfoVo;
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
    RoomDao roomDao;

    @Autowired
    MypageDao mypageDao;

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
            log.info("Socket Rest {} error {}", request_uri, e.getMessage());
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
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int old_state, int state, String authToken, boolean isLogin){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}, {}", roomNo, memNo, old_state, state, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }

            String command = "reqMicOn";
            if(state == 3){ //통화중
                command = "reqCalling";
            }else if(state == 2){ // 마이크 오프
                command = "reqMicOff";
            }else{
                if(old_state == 3){
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
    public Map<String, Object> changeRoomState(String roomNo, String memNo, int state, String authToken, boolean isLogin){
        log.info("Socket Start : changeRoomState {}, {}, {}, {}", roomNo, memNo, state, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }

            String command = "reqMicOn";
            if(state == 3){ //통화중
                command = "reqCalling";
            }else if(state == 2){ // 마이크 오프
                command = "reqMicOff";
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
    public Map<String, Object> changeManager(String roomNo, String memNo, String menagerMemNo, boolean isManager, String authToken, boolean isLogin){
        log.info("Socket Start : changeManager {}, {}, {}, {}, {}", roomNo, memNo, menagerMemNo, isManager, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(menagerMemNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> changeRoomInfo(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin){
        log.info("Socket Start : changeManager {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqRoomChangeInfo");
            vo.setMessage(roomInfo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> changeCount(String roomNo, String memNo, HashMap roomInfo, String authToken, boolean isLogin){
        log.info("Socket Start : changeCount {}, {}, {}, {}", roomNo, memNo, roomInfo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && roomInfo != null && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqChangeCount");
            vo.setMessage(roomInfo);
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendLike(String roomNo, String memNo, boolean isFirst, String authToken, boolean isLogin){
        log.info("Socket Start : sendLike {}, {}, {}, {}", roomNo, memNo, isFirst, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            if(isFirst){
                SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
                if(vo.getFan() == 0){
                    vo.setCommand("reqGoodFirst");
                    vo.setRecvMemNo(memNo);
                    vo.setMessage("{\"time\" : 0}");
                    sendSocketApi(authToken, roomNo, vo.toQueryString());
                }
            }
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqGood");
            return sendSocketApi(authToken, roomNo, vo.toQueryString());
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> sendBooster(String roomNo, String memNo, String authToken, boolean isLogin){
        log.info("Socket Start : sendBooster {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = memNo == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> sendNotice(String roomNo, String memNo, String notice, String authToken, boolean isLogin){
        log.info("Socket Start : sendNotice {}, {}, {}, {}", roomNo, memNo, notice, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        notice = notice == null ? "" : notice.trim();
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> sendStory(String roomNo, String memNo, Object story, String authToken, boolean isLogin){
        log.info("Socket Start : sendStory {}, {}, {}, {}", roomNo, memNo, story, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && story != null){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> addFan(String roomNo, String memNo, String recvMemNo, String authToken, String fan, boolean isLogin){
        log.info("Socket Start : addFan {}, {}, {}, {}, {}", roomNo, memNo, recvMemNo, fan, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> giftItem(String roomNo, String memNo, String giftedMemNo, Object item, String authToken, boolean isLogin){
        log.info("Socket Start : giftItem {}, {}, {}, {}, {}", roomNo, memNo, giftedMemNo, item, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken) && item != null){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> kickout(String roomNo, String memNo, String kickedMemNo, String authToken, boolean isLogin){
        log.info("Socket Start : kickout {}, {}, {}, {}", roomNo, memNo, kickedMemNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        kickedMemNo = kickedMemNo == null ? "" : kickedMemNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(kickedMemNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("reqKickOut");
            HashMap kickedMemInfo = getMyInfo(kickedMemNo);
            if(kickedMemInfo != null){
                HashMap socketMap = new HashMap();
                socketMap.put("sndAuth", vo.getAuth());
                socketMap.put("sndMemNo", vo.getMemNo());
                socketMap.put("sndMemNk", vo.getMemNk());
                socketMap.put("revMemNo", kickedMemNo);
                socketMap.put("revMemNk", DalbitUtil.getStringMap(kickedMemInfo, "nickName"));
                vo.setMessage(socketMap);
                return sendSocketApi(authToken, roomNo, vo.toQueryString());
            }
        }
        return null;
    }

    @Async("threadTaskExecutor")
    public Map<String, Object> banWord(String roomNo, String memNo, String authToken, boolean isLogin){
        log.info("Socket Start : kickout {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
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
    public Map<String, Object> chatEnd(String roomNo, String memNo, String authToken, boolean isLogin) {
        log.info("Socket Start : chatEnd {}, {}, {}", roomNo, memNo, isLogin);
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(roomNo) && !"".equals(memNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo, isLogin);
            if(vo.getMemNo() == null){
                return null;
            }
            vo.setCommand("chatEnd");
            vo.setMessage("bjOut");

            log.info(" @@@ chatEnd @@@");
            log.info(vo.toQueryString());
            log.info(" @@@ chatEnd @@@");
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

    public SocketVo getSocketVo(String roomNo, String memNo, boolean isLogin){
        try{
            return new SocketVo(memNo, getUserInfo(roomNo, memNo, isLogin), isLogin);
        }catch(Exception e){e.getStackTrace();}
        return null;
    }


    public HashMap getUserInfo(String roomNo, String memNo, boolean isLogin){
        P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
        pRoomMemberInfoVo.setMem_no(memNo);
        pRoomMemberInfoVo.setRoom_no(roomNo);
        pRoomMemberInfoVo.setTarget_mem_no(memNo);

        try{
            ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
            roomDao.callBroadCastRoomMemberInfo(procedureVo);
            HashMap userInfo = new Gson().fromJson(procedureVo.getExt(), HashMap.class);;

            P_RoomInfoViewVo pRoomInfoViewVo = new P_RoomInfoViewVo();
            pRoomInfoViewVo.setMemLogin(isLogin ? 1 : 0);
            pRoomInfoViewVo.setMem_no(memNo);
            pRoomInfoViewVo.setRoom_no(roomNo);
            ProcedureVo procedureRoomVo = new ProcedureVo(pRoomInfoViewVo);
            P_RoomInfoViewVo roomInfoViewVo = roomDao.callBroadCastRoomInfoView(procedureRoomVo);
            if(!DalbitUtil.isEmpty(roomInfoViewVo)) {
                P_RoomMemberInfoVo pRoomMemberInfoDalDVo = new P_RoomMemberInfoVo();
                pRoomMemberInfoDalDVo.setMem_no(memNo);
                pRoomMemberInfoDalDVo.setRoom_no(roomNo);
                pRoomMemberInfoDalDVo.setTarget_mem_no(roomInfoViewVo.getBj_mem_no());
                ProcedureVo procedureDalDVo = new ProcedureVo(pRoomMemberInfoDalDVo);
                roomDao.callBroadCastRoomMemberInfo(procedureDalDVo);
                HashMap dalDInfo = new Gson().fromJson(procedureDalDVo.getExt(), HashMap.class);
                userInfo.put("enableFan", DalbitUtil.getIntMap(dalDInfo, "enableFan"));
            }

            return userInfo;
        }catch(Exception e){e.getStackTrace();}
        return null;
    }

    public HashMap getMyInfo(String memNo){
        P_MemberInfoVo apiData = new P_MemberInfoVo();
        apiData.setMem_no(memNo);
        ProcedureVo procedureVo = new ProcedureVo(apiData);
        mypageDao.callMemberInfo(procedureVo);
        if(procedureVo.getRet().equals(Status.회원정보보기_성공.getMessageCode())) {
            return new Gson().fromJson(procedureVo.getExt(), HashMap.class);
        }
        return null;
    }
}


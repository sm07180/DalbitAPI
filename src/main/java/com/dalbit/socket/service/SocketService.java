package com.dalbit.socket.service;

import com.dalbit.broadcast.dao.RoomDao;
import com.dalbit.broadcast.vo.procedure.P_RoomMemberInfoVo;
import com.dalbit.common.vo.ProcedureVo;
import com.dalbit.exception.GlobalException;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
            log.debug("Socket Rest {} error {}", request_uri, e.getMessage());
            if(con != null){
                result = readStream(con.getErrorStream());
            }

        }

        log.debug("Socket Result {}, {}, {}", roomNo, params, result);
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

    public Map<String, Object> sendMessage(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo();
        String roomNo = DalbitUtil.convertRequestParamToString(request,"roomNo");
        String message = DalbitUtil.convertRequestParamToString(request,"message");
        String authToken = DalbitUtil.getAuthToken(request);
        memNo = memNo == null ? "" : memNo.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(message) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo);
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

    public Map<String, Object> changeRoomState(String roomNo, String memNo, boolean isMic, boolean isCall, String authToken){
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = memNo == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo);
            if(vo.getMemNo() == null){
                return null;
            }

            String command = "reqMicOn";
            if(isCall){ //통화중
                command = "reqCalling";
            }else{
                if(isMic == false){ // 마이크 오프
                    command = "reqMicOff";
                }else{
                    if(isCall == false) { //마이크는 켜져있는데 통화 종료
                        command = "reqEndCall";
                    }
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

    public Map<String, Object> changeManager(String roomNo, String memNo, String menagerMemNo, boolean isManager, String authToken){
        roomNo = roomNo == null ? "" : roomNo.trim();
        memNo = memNo == null ? "" : memNo.trim();
        authToken = memNo == null ? "" : authToken.trim();

        if(!"".equals(memNo) && !"".equals(menagerMemNo) && !"".equals(roomNo) && !"".equals(authToken)){
            SocketVo vo = getSocketVo(roomNo, memNo);
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

    public SocketVo getSocketVo(String roomNo, String memNo){
        P_RoomMemberInfoVo pRoomMemberInfoVo = new P_RoomMemberInfoVo();
        pRoomMemberInfoVo.setMem_no(memNo);
        pRoomMemberInfoVo.setRoom_no(roomNo);
        pRoomMemberInfoVo.setTarget_mem_no(memNo);

        try{
        ProcedureVo procedureVo = new ProcedureVo(pRoomMemberInfoVo);
            roomDao.callBroadCastRoomMemberInfo(procedureVo);

            return new SocketVo(memNo, new Gson().fromJson(procedureVo.getExt(), HashMap.class));
        }catch(Exception e){e.getStackTrace();}
        return null;
    }
}


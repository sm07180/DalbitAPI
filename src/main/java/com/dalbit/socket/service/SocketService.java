package com.dalbit.socket.service;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import io.github.sac.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class SocketService {
    @Value("${socket.ip}")
    private String SERVER_SOCKET_IP;

    @Value("${socket.port}")
    private String SERVER_SOCKET_PORT;

    @Value("${sso.header.cookie.name}")
    private String SSO_HEADER_COOKIE_NAME;

    public void sendMessage(HttpServletRequest request){
        String memNo = MemberVo.getMyMemNo();
        String roomNo = DalbitUtil.convertRequestParamToString(request,"roomNo");
        String message = DalbitUtil.convertRequestParamToString(request,"message");
        String authToken = request.getHeader(SSO_HEADER_COOKIE_NAME);
        memNo = memNo == null ? "" : memNo.trim();
        authToken = authToken == null ? "" : authToken.trim();
        if(!"".equals(memNo) && !"".equals(roomNo) && !"".equals(message) && !"".equals(authToken)){
            String socketUrl = "ws://" + SERVER_SOCKET_IP + ":" + SERVER_SOCKET_PORT + "/socketcluster/?data=";
            String socketMemInfo = "{memNo:\"" + memNo + "\", authToken:\"" + authToken + "\"locale:\"ko_KR\"}";

            Socket socket = new Socket(socketUrl + socketMemInfo);
            socket.disableLogging();
            socket.setReconnection(new ReconnectStrategy().setDelay(2000).setMaxAttempts(30));
            socket.connect();

            HashMap<String, Object> chat = new HashMap<>();
            chat.put("memNo", "");

            HashMap<String, Object> data = new HashMap<>();
            data.put("cmd", "chat");
            data.put("chat", chat);
            data.put("msg", message);

            socket.publish("chanel." + roomNo, data, new Ack() {
                public void call(String channelName, Object error, Object data) {
                    if (error == null) {
                        log.debug("sent message : " + data.toString());
                    }else{
                        log.debug(error.toString());
                    }
                }
            });//chanel.[roomNo]

            socket.disconnect();
        }
    }
}

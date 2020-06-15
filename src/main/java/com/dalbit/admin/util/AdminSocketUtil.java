package com.dalbit.admin.util;

import com.dalbit.socket.service.SocketService;
import com.dalbit.socket.vo.SocketVo;
import com.dalbit.util.DalbitUtil;
import com.dalbit.util.GsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


// reqMicOn             // 마이크 온
// reqCalling           // 통화중
// reqMicOff            // 마이크 오프
// reqEndCall           // ?
// reqGrant             // 권한 변경
// reqRoomChangeInfo    // 방정보 변경
// reqChangeCount       //
// reqGoodFirst         // 좋아요
// reqBooster           // 부스터
// reqNotice            // 알림
// reqStory             // 사연
// reqFan               // 팬
// reqGiftDal
// reqGiftImg           // 선물 이미지
// reqGuest             // 게스트
// reqKickOut           // 강퇴
// reqBanWord
// chatEnd              // 방폭
// reqMyInfo


@Component
public class AdminSocketUtil {

    /**
     * socketUtil
     */
    @Autowired
    SocketService socketService;
    @Autowired
    GsonUtil gsonUtil;

    @Value("${socket.global.room}")
    private String SERVER_SOCKET_GLOBAL_ROOM;

    public Map<String, Object> setSocket(HashMap<String,Object> param ,String command, String message, String authToken){
        if(!"".equals(param) && !"".equals(authToken)) {
            if(command.equals("chatEnd")){

                HashMap<String,String> map = new HashMap<>();
                map.put("authToken","");
                map.put("message",message);

                message = gsonUtil.toJson(map);
                SocketVo vo = getSocketVo(param, command, message);
                System.out.println(vo.toQueryString());
                socketService.sendSocketApi(authToken, DalbitUtil.getStringMap(param, "roomNo"), vo.toQueryString());
            }else if(command.equals("reqKickOut")) {
                SocketVo vo = getSocketVo(param, command, message);
                System.out.println(vo.toQueryString());
                socketService.sendSocketApi(authToken, DalbitUtil.getStringMap(param, "roomNo"), vo.toQueryString());
            }else{
                SocketVo vo = getSocketVo(param, command, message);
                System.out.println(vo.toQueryString());
                socketService.sendSocketApi(authToken, SERVER_SOCKET_GLOBAL_ROOM, vo.toQueryString());
            }
        }
        return null;
    }


    public SocketVo getSocketVo(HashMap<String,Object> param, String command, String message){
        try {
            SocketVo socketVo = new SocketVo();

            socketVo.setMessage(message);
            socketVo.setCommand(command);
            socketVo.setMemImg("null");
            if (command.equals("reqKickOut")){
                socketVo.setMemNo(DalbitUtil.getStringMap(param, "target_memNo"));
                socketVo.setMemNk(DalbitUtil.getStringMap(param, "target_nickName"));
            }else{
                socketVo.setMemNo(DalbitUtil.getStringMap(param, "memNo"));
                socketVo.setMemNk(DalbitUtil.getStringMap(param, "memNk"));
            }
            socketVo.setFan(1);
            socketVo.setAuth(0);
            socketVo.setAuthName("");
            socketVo.setCtrlRole(DalbitUtil.getStringMap(param, "ctrlRole"));
            socketVo.setRecvMemNo(DalbitUtil.getStringMap(param, "recvMemNo"));
            socketVo.setRecvType(DalbitUtil.getStringMap(param, "recvType"));
            socketVo.setRecvPosition(DalbitUtil.getStringMap(param, "recvPosition"));
            socketVo.setRecvLevel(DalbitUtil.getIntMap(param, "recvLevel"));
            socketVo.setRecvTime(DalbitUtil.getIntMap(param, "recvTime"));
            socketVo.setLogin(1);
            socketVo.setRecvDj(1);
            socketVo.setRecvManager(1);
            socketVo.setRecvListener(1);

            return socketVo;
        }catch(Exception e){e.getStackTrace();}
        return null;
    }
}

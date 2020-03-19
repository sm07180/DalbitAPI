package com.dalbit.socket.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;


/**
 * Socket 파라메터 정의
 *
 * command : 명령값
 * message : 메세지내용
 * memNo : 보낸이(회원번호)
 * fan : 보낸이 팬여부(0,1)
 * auth : 보낸이 등급(0:청취자,1:매니져,2:게스트,3:DJ)
 * authName : 보낸이 등급명
 * ctrlRole : 보낸이 권한(string)
 * login : 보낸이 로그인상태(1,0)
 * recvMemNo : 받는이(공백 또는 회원번호,구분)
 * recvDj : Dj메세지수신여부(1,0)
 * recvManager : Manager메세지수신여부(0,1)
 * recvListener : Listener메세지수신여부(0,1)
 * recvType : 메세지구분(system,chat)
 * recvPosition : 메세지표시영역(top1, top2, top3, chat)
 * recvLevel : 메세지레이어번호(0(채팅),1~4)
 * recvTime : 메세지노출시간(초)
 */
@Setter @Getter @ToString
public class SocketVo {
    private String command;
    private String message;
    private String memNo;
    private String memNk;
    private String memImg;
    private int fan = 0;
    private int auth = 0;
    private String authName;
    private String ctrlRole;
    private int login;
    private String recvMemNo;
    private int recvDj = 1;
    private int recvManager = 1;
    private int recvListener = 1;
    private String recvType = "chat";
    private String recvPosition  = "chat";
    private int recvLevel = 0;
    private int recvTime = 0;


    public SocketVo(String memNo, String recvMemNo, boolean isLogin){
        this.memNo = memNo;
        this.recvMemNo = recvMemNo;
        this.login = isLogin ? 1 : 0;
    }

    public SocketVo(String memNo, HashMap memInfo, boolean isLogin){
        if(memInfo != null){
            this.memNo = memNo;
            this.fan = DalbitUtil.getIntMap(memInfo, "enableFan") == 0 ? 1 : 0;
            this.auth = DalbitUtil.getIntMap(memInfo, "auth");
            this.authName = "청취자";
            if(this.auth == 3){
                this.authName = "달D";
            }else if(this.auth == 2){
                this.authName = "게스트";
            }else if(this.auth == 1){
                this.authName = "매니저";
            }
            ImageVo img = new ImageVo(DalbitUtil.getStringMap(memInfo, "profileImage"), DalbitUtil.getStringMap(memInfo, "memSex"), DalbitUtil.getProperty("server.photo.url"));
            this.memImg = img.getThumb120x120();
            this.login = isLogin ? 1 : 0;
            this.ctrlRole = DalbitUtil.getStringMap(memInfo, "controlRole");
            this.memNk = DalbitUtil.getStringMap(memInfo, "nickName");
        }
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setMessage(Object message){
        this.message = new Gson().toJson(message);
        //this.message = message;
    }

    public String toQueryString(){
        StringBuffer qs = new StringBuffer();
        qs.append("command=");
        qs.append(this.command);
        qs.append("&memNo=");
        qs.append(this.memNo);
        qs.append("&memNk=");
        qs.append(this.memNk);
        qs.append("&memImg=");
        qs.append(this.memImg);
        qs.append("&message=");
        qs.append(this.message);
        qs.append("&fan=");
        qs.append(this.fan);
        qs.append("&auth=");
        qs.append(this.auth);
        qs.append("&authName=");
        qs.append(this.authName);
        qs.append("&ctrlRole=");
        qs.append(this.ctrlRole == null ? "" : this.ctrlRole);
        qs.append("&login=");
        qs.append(this.login);
        qs.append("&recvMemNo=");
        qs.append(this.recvMemNo == null ? "" : this.recvMemNo);
        qs.append("&recvDj=");
        qs.append(this.recvDj);
        qs.append("&recvManager=");
        qs.append(this.recvManager);
        qs.append("&recvListener=");
        qs.append(this.recvListener);
        qs.append("&recvType=");
        qs.append(this.recvType);
        qs.append("&recvPosition=");
        qs.append(this.recvPosition);
        qs.append("&recvLevel=");
        qs.append(this.recvLevel);
        qs.append("&recvTime=");
        qs.append(this.recvTime);

        return qs.toString();
    }
}

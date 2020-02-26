package com.dalbit.socket.vo;

import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;

@Setter @Getter @ToString
public class SocketVo {
    private String command;
    private String message;
    private String memNo;
    private boolean isFan = false;
    private int auth = 0;
    private String ctrlRole;
    private String recvMemNo;


    public SocketVo(String memNo, HashMap memInfo){
        if(memInfo != null){
            this.memNo = memNo;
            this.isFan = "0".equals(DalbitUtil.getStringMap(memInfo, "enableFan"));
            this.auth = DalbitUtil.getIntMap(memInfo, "auth");
            this.ctrlRole = DalbitUtil.getStringMap(memInfo, "controlRole");
        }
    }

    public void setMessage(Object message){
        this.message = message.toString();
    }

    public String toQueryString(){
        StringBuffer qs = new StringBuffer();
        qs.append("command=");
        qs.append(this.command);
        qs.append("&message=");
        qs.append(this.message);
        qs.append("&memNo=");
        qs.append(this.memNo);
        qs.append("&isFan=");
        qs.append(this.isFan);
        qs.append("&auth=");
        qs.append(this.auth);
        qs.append("&ctrlRole=");
        qs.append(this.ctrlRole == null ? "" : this.ctrlRole);
        qs.append("&recvMemNo=");
        qs.append(this.recvMemNo == null ? "" : this.recvMemNo);

        return qs.toString();
    }
}

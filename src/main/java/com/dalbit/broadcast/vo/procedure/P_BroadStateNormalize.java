package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_BroadStateNormalize {
    private String mem_no;
    private String room_no;
    private int os;
    private String deviceUuid;
    private String ip;
    private String appVersion;

    public P_BroadStateNormalize(){}
    public P_BroadStateNormalize(HttpServletRequest request){
        this.mem_no = MemberVo.getMyMemNo(request);
        this.room_no = request.getParameter("roomNo");
        DeviceVo deviceVo = new DeviceVo(request);
        this.os = deviceVo.getOs();
        this.deviceUuid = deviceVo.getDeviceUuid();
        this.ip = deviceVo.getIp();
        this.appVersion = deviceVo.getAppVersion();
    }
}

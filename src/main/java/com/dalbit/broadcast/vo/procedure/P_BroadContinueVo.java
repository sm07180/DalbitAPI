package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_BroadContinueVo {

    public P_BroadContinueVo(){}
    public P_BroadContinueVo(HttpServletRequest request){
        DeviceVo deviceVo = new DeviceVo(request);
        setMem_no(MemberVo.getMyMemNo(request));
        setOs(deviceVo.getOs());
        setAppVersion(deviceVo.getAppVersion());
        setDeviceUuid(deviceVo.getDeviceUuid());
    }

    private String mem_no;
    private int os;
    private String appVersion;
    private String deviceUuid;
}

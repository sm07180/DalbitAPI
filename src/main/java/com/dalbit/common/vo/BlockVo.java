package com.dalbit.common.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockVo {

    public BlockVo(){}

    public BlockVo(DeviceVo deviceVo){
        this.ip = deviceVo.getIp();
        this.deviceUuid = deviceVo.getDeviceUuid();
    }

    public BlockVo(DeviceVo deviceVo, String memNo){
        this.ip = deviceVo.getIp();
        this.deviceUuid = deviceVo.getDeviceUuid();
        this.memNo = memNo;
    }

    private String ip;
    private String deviceUuid;
    private String memNo;

}

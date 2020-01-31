package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberSessionUpdateVo {

    P_MemberSessionUpdateVo(){}

    public P_MemberSessionUpdateVo(int memLogin, String mem_no, int os, String adId, String deviceUuid, String deviceToken, String appVersion, String location){
        setMemLogin(memLogin);
        setMem_no(mem_no);
        setOs(os);
        setAdId(adId);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setLocation(location);
    }

    private int memLogin;
    private String mem_no;
    private int os;
    private String adId;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String location;
}
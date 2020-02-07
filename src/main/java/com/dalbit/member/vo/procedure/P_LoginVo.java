package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_LoginVo extends BaseVo {

    public P_LoginVo(){}

    //회원용
    public P_LoginVo(String memSlct, String id, String pw, int os, String deviceUuid, String deviceToken, String appVersion, String adId, String location){
        setMemSlct(memSlct);
        setId(id);
        setPw(pw);
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
        setLocation(location);
    }

    //비회원용
    public P_LoginVo(String memSlct, int os, String deviceUuid, String deviceToken, String appVersion, String adId, String location){
        setMemSlct(memSlct);
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
        setLocation(location);
    }

    private String memSlct;
    private String id;
    private String pw;
    private int os;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String adId;
    private String location;
}

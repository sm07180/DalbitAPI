package com.dalbit.member.vo;

import com.dalbit.common.vo.BaseVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_LoginVo extends BaseVo {

    public P_LoginVo(){}

    public P_LoginVo(String memSlct, String id, String pw, int os, String deviceUuid, String deviceToken, String appVersion, String adId){
        setMemSlct(memSlct);
        setId(id);
        setPw(pw);
        setOs(os);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setAdId(adId);
    }

    private String memSlct;
    private String id;
    private String pw;
    private int os;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String adId;
}

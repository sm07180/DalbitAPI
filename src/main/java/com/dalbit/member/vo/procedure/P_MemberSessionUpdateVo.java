package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberSessionUpdateVo extends P_ApiVo {

    P_MemberSessionUpdateVo(){}

    public P_MemberSessionUpdateVo(int memLogin, String mem_no, int os, String adId, String deviceUuid, String deviceToken, String appVersion, String location, String ip, String browser){
        setMemLogin(memLogin);
        setMem_no(mem_no);
        setOs(os);
        setAdId(adId);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setLocation(location);
        setIp(ip);
        setBrowser(browser);
    }

    public P_MemberSessionUpdateVo(int memLogin, String mem_no, int os, String adId, String deviceUuid, String deviceToken, String appVersion, String location, String ip, String browser, String deviceManufacturer, String deviceModel, String sdkVersion, String appBuild){
        setMemLogin(memLogin);
        setMem_no(mem_no);
        setOs(os);
        setAdId(adId);
        setDeviceUuid(deviceUuid);
        setDeviceToken(deviceToken);
        setAppVersion(appVersion);
        setLocation(location);
        setIp(ip);
        setBrowser(browser);
        setDeviceManufacturer(deviceManufacturer);
        setDeviceModel(deviceModel);
        setSdkVersion(sdkVersion);
        setBuildVersion(appBuild);
    }

    private int memLogin;
    private String mem_no;
    private int os;
    private String adId;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String buildVersion;
    private String location;
    private String ip;
    private String browser;
    private String deviceManufacturer;
    private String deviceModel;
    private String sdkVersion;
}
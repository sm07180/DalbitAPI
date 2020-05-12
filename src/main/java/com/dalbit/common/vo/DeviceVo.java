package com.dalbit.common.vo;

import com.dalbit.util.CookieUtil;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.HashMap;

@Getter
@Setter
public class DeviceVo {
    private int os;
    private String deviceUuid;
    private String deviceToken;
    private String appVersion;
    private String appBuild;
    private String adId;
    private String ip;
    private String isHybrid;

    public DeviceVo(HttpServletRequest request){
        this.os = DalbitUtil.convertRequestParamToInteger(request,"os");
        this.deviceUuid = DalbitUtil.convertRequestParamToString(request,"deviceId");
        this.deviceToken = DalbitUtil.convertRequestParamToString(request,"deviceToken");
        this.appVersion = DalbitUtil.convertRequestParamToString(request,"appVer");
        this.appBuild = DalbitUtil.convertRequestParamToString(request,"appBuild");
        this.adId = DalbitUtil.convertRequestParamToString(request,"appAdId");
        this.ip = DalbitUtil.getIp(request);
        this.isHybrid = DalbitUtil.convertRequestParamToString(request, "isHybrid");

        if(DalbitUtil.isEmpty(this.appVersion)){
            appVersion = DalbitUtil.convertRequestParamToString(request,"appVersion");
        }

        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        if(customHeader != null && !"".equals(customHeader.trim())){
            customHeader = URLDecoder.decode(customHeader);
            HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);

            if(!DalbitUtil.isEmpty(headers.get("os")) && !DalbitUtil.isEmpty(headers.get("deviceId"))){
                if(this.os == -1){
                    this.os = (int)DalbitUtil.getDoubleMap(headers, "os");
                }
                if(DalbitUtil.isEmpty(this.deviceUuid)){
                    this.deviceUuid = DalbitUtil.getStringMap(headers, "deviceId");
                }
                if(DalbitUtil.isEmpty(this.deviceToken)) {
                    this.deviceToken = DalbitUtil.getStringMap(headers, "deviceToken");
                }
                if(DalbitUtil.isEmpty(this.appVersion)) {
                    this.appVersion = DalbitUtil.getStringMap(headers, "appVer");
                    if(DalbitUtil.isEmpty(this.appVersion)){
                        this.appVersion = DalbitUtil.getStringMap(headers, "appVersion");
                    }
                }
                if(DalbitUtil.isEmpty(this.adId)) {
                    this.adId = DalbitUtil.getStringMap(headers, "appAdId");
                }
                if(DalbitUtil.isEmpty(this.adId)) {
                    this.isHybrid = DalbitUtil.getStringMap(headers, "isHybrid");
                }
                if(DalbitUtil.isEmpty(this.appBuild)) {
                    String appBulid = DalbitUtil.getStringMap(headers, "appBuild");
                    // ios 오타로 인한 추가 체크 (심사 올라간 버전이라 수정 불가)
                    if(os == 2 && DalbitUtil.isEmpty(appBulid)) {
                        appBulid = DalbitUtil.getStringMap(headers, "appBulid");
                    }
                    this.appBuild = appBulid;
                }
            }
        }

        // 혹시나해서 쿠키 값으로 도 확인
        String cookieHeader = "";
        try{
            CookieUtil cookieUtil = new CookieUtil(request);
            cookieHeader = cookieUtil.getValue(DalbitUtil.getProperty("rest.custom.header.name"));
        }catch(IOException e){}
        if(!DalbitUtil.isEmpty(cookieHeader)){
            HashMap<String, Object> headers = new Gson().fromJson(cookieHeader, HashMap.class);

            if(!DalbitUtil.isEmpty(headers.get("os")) && !DalbitUtil.isEmpty(headers.get("deviceId"))){
                if(this.os == -1){
                    this.os = (int)DalbitUtil.getDoubleMap(headers, "os");
                }
                if(DalbitUtil.isEmpty(this.deviceUuid)){
                    this.deviceUuid = DalbitUtil.getStringMap(headers, "deviceId");
                }
                if(DalbitUtil.isEmpty(this.deviceToken)) {
                    this.deviceToken = DalbitUtil.getStringMap(headers, "deviceToken");
                }
                if(DalbitUtil.isEmpty(this.appVersion)) {
                    this.appVersion = DalbitUtil.getStringMap(headers, "appVer");
                    if(DalbitUtil.isEmpty(this.appVersion)){
                        this.appVersion = DalbitUtil.getStringMap(headers, "appVersion");
                    }
                }
                if(DalbitUtil.isEmpty(this.adId)) {
                    this.adId = DalbitUtil.getStringMap(headers, "appAdId");
                }
                if(DalbitUtil.isEmpty(this.adId)) {
                    this.isHybrid = DalbitUtil.getStringMap(headers, "isHybrid");
                }
                if(DalbitUtil.isEmpty(this.appBuild)) {
                    String appBulid = DalbitUtil.getStringMap(headers, "appBuild");
                    // ios 오타로 인한 추가 체크 (심사 올라간 버전이라 수정 불가)
                    if(os == 2) {
                        appBulid = DalbitUtil.getStringMap(headers, "appBulid");
                    }
                    this.appBuild = appBulid;
                }
            }
        }
        this.deviceToken = DalbitUtil.isNullToString(deviceToken);
        this.appVersion = DalbitUtil.isNullToString(appVersion);
        this.adId = DalbitUtil.isNullToString(adId);

        if(!DalbitUtil.isEmpty(this.appBuild) && this.appBuild.indexOf(".") > -1){
            this.appBuild = this.appBuild.substring(0, this.appBuild.indexOf("."));
        }
        if("192.168.10.163".equals(this.ip) || "192.168.10.164".equals(this.ip)){ //소켓서버일경우 3번으로 수정
            this.os =3;
        }
    }
}

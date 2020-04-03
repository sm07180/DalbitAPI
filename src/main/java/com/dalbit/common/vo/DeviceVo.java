package com.dalbit.common.vo;

import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
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
        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
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

        if(customHeader != null && !"".equals(customHeader.trim())){

            customHeader = java.net.URLDecoder.decode(customHeader);
            HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);

            if(!DalbitUtil.isEmpty(headers.get("os")) && !DalbitUtil.isEmpty(headers.get("deviceId"))){
                this.os = (int)DalbitUtil.getDoubleMap(headers, "os");
                this.deviceUuid = DalbitUtil.getStringMap(headers, "deviceId");
                this.deviceToken = DalbitUtil.getStringMap(headers, "deviceToken");
                this.appVersion = DalbitUtil.getStringMap(headers, "appVer");
                this.adId = DalbitUtil.getStringMap(headers, "appAdId");
                if(DalbitUtil.isEmpty(this.appVersion)){
                    this.appVersion = DalbitUtil.getStringMap(headers, "appVersion");
                }
                this.appBuild = DalbitUtil.getStringMap(headers, "appBuild");
                this.deviceToken = DalbitUtil.isNullToString(deviceToken);
                this.appVersion = DalbitUtil.isNullToString(appVersion);
                this.adId = DalbitUtil.isNullToString(adId);
                this.isHybrid = DalbitUtil.getStringMap(headers, "isHybrid");
            }
        }
    }
}

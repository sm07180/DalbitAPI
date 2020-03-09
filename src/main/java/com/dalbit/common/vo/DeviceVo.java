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
    private String adId;
    private String ip;
    private String isHybrid;

    public DeviceVo(HttpServletRequest request){
        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        this.os = DalbitUtil.convertRequestParamToInteger(request,"os");
        this.deviceUuid = DalbitUtil.convertRequestParamToString(request,"deviceId");
        this.deviceToken = DalbitUtil.convertRequestParamToString(request,"deviceToken");
        this.appVersion = DalbitUtil.convertRequestParamToString(request,"appVer");
        this.adId = DalbitUtil.convertRequestParamToString(request,"appAdId");
        this.ip = DalbitUtil.getIp(request);
        this.isHybrid = DalbitUtil.convertRequestParamToString(request, "isHybrid");

        if(customHeader != null && !"".equals(customHeader.trim())){

            customHeader = java.net.URLDecoder.decode(customHeader);
            HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);

            if(!DalbitUtil.isEmpty(headers.get("os")) && !DalbitUtil.isEmpty(headers.get("deviceId"))){
                os = (int)DalbitUtil.getDoubleMap(headers, "os");
                deviceUuid = DalbitUtil.getStringMap(headers, "deviceId");
                deviceToken = DalbitUtil.getStringMap(headers, "deviceToken");
                appVersion = DalbitUtil.getStringMap(headers, "appVer");
                adId = DalbitUtil.getStringMap(headers, "appAdId");
                deviceToken = DalbitUtil.isNullToString(deviceToken);
                appVersion = DalbitUtil.isNullToString(appVersion);
                adId = DalbitUtil.isNullToString(adId);
                isHybrid = DalbitUtil.getStringMap(headers, "isHybrid");

            }
        }
    }
}

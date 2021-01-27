package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.ReportImageVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Getter @Setter @ToString
public class P_ReportImageVo {
    public P_ReportImageVo(){}
    public P_ReportImageVo(ReportImageVo reportImageVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setReported_mem_no(reportImageVo.getMemNo());
        setImage_type(reportImageVo.getImageType());
        setImage_idx(reportImageVo.getImageIdx());
        setImage_url(reportImageVo.getImagePath());
        setRoom_no(reportImageVo.getRoomNo());
        setReason(reportImageVo.getReason());
        setEtc(reportImageVo.getEtc());
        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        customHeader = java.net.URLDecoder.decode(customHeader);
        HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);
        int os = DalbitUtil.getIntMap(headers,"os");
        String isHybrid = DalbitUtil.getStringMap(headers,"isHybrid");
        String platform="";
        if(os == 1){
            platform = "Android-Mobile";
        }else if(os == 2){
            platform = "IOS-Mobile";
        } else if(os == 3){
            platform = "PC";
        } else if(isHybrid.equals("Y")){
            platform = "Web-Mobile";
        }
        setPlatform(platform);
        setBrowser(DalbitUtil.getUserAgent(request));
        setIp(new DeviceVo(request).getIp());
    }

    private String mem_no;
    private String reported_mem_no;
    private int image_type;
    private String image_idx;
    private String image_url;
    private String room_no;
    private int reason;
    private String etc;
    private String platform;
    private String browser;
    private String ip;
}

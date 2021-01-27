package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipDeclarVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Slf4j
@Getter
@Setter
public class P_ClipDeclarVo extends P_ApiVo {

    private String mem_no;
    private String reported_mem_no;                           //신고회원번호
    private Integer reason;                                   //신고사유
    private String etc;                                       //기타내용

    private String room_no;
    private String platform;
    private String browser;
    private String ip;

    public P_ClipDeclarVo() { }
    public P_ClipDeclarVo(ClipDeclarVo clipDeclarVo, DeviceVo deviceVo, HttpServletRequest request) {
        setMem_no(MemberVo.getMyMemNo(request));
        setReported_mem_no(clipDeclarVo.getMemNo());
        setReason(clipDeclarVo.getReason());
        setEtc(clipDeclarVo.getCont());
        setRoom_no(clipDeclarVo.getClipNo());
        setIp(deviceVo.getIp());

        String customHeader = request.getHeader(DalbitUtil.getProperty("rest.custom.header.name"));
        customHeader = java.net.URLDecoder.decode(customHeader);
        HashMap<String, Object> headers = new Gson().fromJson(customHeader, HashMap.class);
        int os = DalbitUtil.getIntMap(headers,"os");
        String isHybrid = DalbitUtil.getStringMap(headers,"isHybrid");

        if(os == 1){
            setPlatform("Android-Mobile");
        }else if(os == 2){
            setPlatform("IOS-Mobile");
        } else if(os == 3){
            setPlatform("PC");
        } else if(isHybrid.equals("Y")){
            setPlatform("Web-Mobile");
        } else {
            setPlatform("PC");
        }

        setBrowser(DalbitUtil.getUserAgent(request));
    }

}

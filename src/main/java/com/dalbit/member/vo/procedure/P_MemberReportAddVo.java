package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MemberReportAddVo;
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
public class P_MemberReportAddVo {

    private String mem_no;
    private String reported_mem_no;                           //신고회원번호
    private Integer reason;                                   //신고사유
    private String etc;                                       //기타내용

    private String room_no;
    private String platform;
    private String browser;
    private String ip;

    public P_MemberReportAddVo() { }
    public P_MemberReportAddVo(MemberReportAddVo memberReportAddVo, DeviceVo deviceVo, HttpServletRequest request) {
        setMem_no(new MemberVo().getMyMemNo(request));
        setReported_mem_no(memberReportAddVo.getMemNo());
        setReason(memberReportAddVo.getReason());
        setEtc(memberReportAddVo.getCont());
        setRoom_no(memberReportAddVo.getRoomNo());
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

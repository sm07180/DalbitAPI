package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.main.vo.request.NoticeListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_NoticeListVo extends P_ApiVo {

    public P_NoticeListVo(){}
    public P_NoticeListVo(NoticeListVo noticeListVo, HttpServletRequest request, MemberVo memberVo){
        int pageNo = DalbitUtil.isEmpty(noticeListVo.getPage()) ? 1 : noticeListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(noticeListVo.getRecords()) ? 10 : noticeListVo.getRecords();

        setSlctType(noticeListVo.getNoticeType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
        setMem_no(MemberVo.getMyMemNo(request));

        DeviceVo deviceVo = new DeviceVo(request);
        setOs(deviceVo.getOs());

        /*if(deviceVo.getOs()==1){
            setPlatform("010");
        } else if(deviceVo.getOs()==2) {
            setPlatform("001");
        }else if(deviceVo.getOs()==3){
            setPlatform("100");
        }
        *//*else if(deviceVo.getIsHybrid().equals("Y")){
            setPlatform(5);
        }*//*
        else {
            setPlatform("111");
        }*/

        if(DalbitUtil.isEmpty(memberVo) || DalbitUtil.isEmpty(memberVo.getGender())){
            setGender(1);
        }else if(!DalbitUtil.isEmpty(memberVo.getGender())) {
            setGender(memberVo.getGender().equals("m") ? 2 : 3);
        }
    }

    /* Input */
    private Integer slctType;
    private String platform;
    private int os;
    private int gender;
    private int pageNo;
    private int pageCnt;
    private String mem_no;

    /* Output */
    private int noticeIdx;
    private String title;
    private String contents;
    private int topFix;
    private Date writeDate;
    private int isNew;
}

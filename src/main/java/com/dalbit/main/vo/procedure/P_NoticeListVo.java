package com.dalbit.main.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.main.vo.request.NoticeListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_NoticeListVo {

    public P_NoticeListVo(){}
    public P_NoticeListVo(NoticeListVo noticeListVo, HttpServletRequest request, MemberVo memberVo){
        int pageNo = DalbitUtil.isEmpty(noticeListVo.getPage()) ? 1 : noticeListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(noticeListVo.getRecords()) ? 10 : noticeListVo.getRecords();

        setSlctType(noticeListVo.getNoticeType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);

        DeviceVo deviceVo = new DeviceVo(request);
        if(deviceVo.getOs()==1){
            setPlatform(3);
        } else if(deviceVo.getOs()==2) {
            setPlatform(4);
        }else if(deviceVo.getOs()==3){
            setPlatform(2);
        } else if(deviceVo.getIsHybrid().equals("Y")){
            setPlatform(5);
        } else {
            setPlatform(1);
        }

        if(!DalbitUtil.isEmpty(memberVo.getGender())) {
            setGender(memberVo.getGender().equals("m") ? 2 : 3);
        } else {
            setGender(1);
        }
    }

    /* Input */
    private Integer slctType;
    private int platform;
    private int gender;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int noticeIdx;
    private String title;
    private int topFix;
    private Date writeDate;

}

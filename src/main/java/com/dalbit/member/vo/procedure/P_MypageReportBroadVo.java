package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageReportBroadVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_MypageReportBroadVo {

    public P_MypageReportBroadVo(){}
    public P_MypageReportBroadVo(MypageReportBroadVo mypageReportBroadVo){
        int pageNo = DalbitUtil.isEmpty(mypageReportBroadVo.getPage()) ? 1 : mypageReportBroadVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mypageReportBroadVo.getRecords()) ? 10 : mypageReportBroadVo.getRecords();

        setMem_no(MemberVo.getMyMemNo());
        setSlct_type(mypageReportBroadVo.getDateType());
        setStartDate(DalbitUtil.convertDate(mypageReportBroadVo.getStartDt(), "yyyy-MM-dd"));
        setEndDate(DalbitUtil.convertDate(mypageReportBroadVo.getEndDt(), "yyyy-MM-dd"));
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private Integer slct_type;
    private String startDate;
    private String endDate;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private Date broadcastDate;
    private Date start_date;
    private Date end_date;
    private int airTime;
    private int gold;
    private int good;
    private int listener;
    private int rank;


}

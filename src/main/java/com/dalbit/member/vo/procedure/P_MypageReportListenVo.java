package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageReportListenVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_MypageReportListenVo extends P_ApiVo {

    public P_MypageReportListenVo(){}
    public P_MypageReportListenVo(MypageReportListenVo mypageReportListenVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mypageReportListenVo.getPage()) ? 1 : mypageReportListenVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mypageReportListenVo.getRecords()) ? 10 : mypageReportListenVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(mypageReportListenVo.getDateType());
        setStartDate(DalbitUtil.convertDate(mypageReportListenVo.getStartDt(), "yyyy-MM-dd"));
        setEndDate(DalbitUtil.convertDate(mypageReportListenVo.getEndDt(), "yyyy-MM-dd"));
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
    private Date listenDate;
    private Date start_date;
    private Date end_date;
    private String dj_nickname;
    private int listentime;
    private int gift_ruby;
    private int gold;
    private int guest;


}

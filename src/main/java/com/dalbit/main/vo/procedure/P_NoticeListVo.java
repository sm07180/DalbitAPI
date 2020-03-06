package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.NoticeListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_NoticeListVo {

    public P_NoticeListVo(){}
    public P_NoticeListVo(NoticeListVo noticeListVo){
        int pageNo = DalbitUtil.isEmpty(noticeListVo.getPage()) ? 1 : noticeListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(noticeListVo.getRecords()) ? 10 : noticeListVo.getRecords();

        setSlct_type(noticeListVo.getNoticeType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private Integer slct_type;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int noticeIdx;
    private int slctType;
    private String title;
    private int topFix;
    private Date writeDate;

}

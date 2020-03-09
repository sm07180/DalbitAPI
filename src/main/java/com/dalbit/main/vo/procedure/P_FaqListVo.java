package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.FaqListVo;
import com.dalbit.main.vo.request.NoticeListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class P_FaqListVo {

    public P_FaqListVo(){}
    public P_FaqListVo(FaqListVo faqListVo){
        int pageNo = DalbitUtil.isEmpty(faqListVo.getPage()) ? 1 : faqListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(faqListVo.getRecords()) ? 10 : faqListVo.getRecords();

        setSlctType(faqListVo.getFaqType() == 0 ? -1 : faqListVo.getFaqType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private Integer slctType;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int faqIdx;
    private String question;
    private Date writeDate;

}

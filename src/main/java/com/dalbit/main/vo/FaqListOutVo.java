package com.dalbit.main.vo;

import com.dalbit.main.vo.procedure.P_FaqListVo;
import com.dalbit.main.vo.procedure.P_NoticeListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FaqListOutVo {

    private int faqIdx;
    private int faqType;
    private String question;
    private String writeDt;
    private Long writeTs;

    public FaqListOutVo(P_FaqListVo target) {
        setFaqIdx(target.getFaqIdx());
        setFaqType(target.getSlctType());
        setQuestion(target.getQuestion());
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
    }
}

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
    private String title;
    private boolean isTop;
    private String writeDt;
    private Long writeTs;

    public FaqListOutVo(P_FaqListVo target) {
        setFaqIdx(target.getFaqIdx());
        setFaqType(target.getSlctType());
        setTitle(target.getTitle());
        setTop(target.getTopFix() == 1 ? true : false);
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
    }
}

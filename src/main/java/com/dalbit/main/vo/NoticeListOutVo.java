package com.dalbit.main.vo;

import com.dalbit.main.vo.procedure.P_NoticeListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeListOutVo {

    private int noticeIdx;
    private int noticeType;
    private String title;
    private boolean isTop;
    private String writeDt;
    private Long writeTs;

    public NoticeListOutVo() {}
    public NoticeListOutVo(P_NoticeListVo target) {
        setNoticeIdx(target.getNoticeIdx());
        setNoticeType(target.getSlctType());
        setTitle(target.getTitle());
        setTop(target.getTopFix() == 1 ? true : false);
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
    }
}

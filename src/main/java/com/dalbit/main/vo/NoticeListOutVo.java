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
    private String contents;
    private boolean isTop;
    private String writeDt;
    private Long writeTs;
    private boolean isNew;
    private String read_yn;

    public NoticeListOutVo() {}
    public NoticeListOutVo(P_NoticeListVo target) {
        setNoticeIdx(target.getNoticeIdx());
        setNoticeType(target.getSlctType());
        setTitle(target.getTitle());
        setContents(target.getContents());
        setTop(target.getTopFix() == 1);
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
        setRead_yn(target.getRead_yn());
        this.isNew = target.getIsNew() == 1;
    }
}

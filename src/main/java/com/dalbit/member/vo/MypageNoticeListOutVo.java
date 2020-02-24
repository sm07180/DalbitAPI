package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_MypageNoticeSelectVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MypageNoticeListOutVo {

    private Long noticeIdx;
    private String title;
    private String contents;
    private boolean isTop;
    private String writeDt;
    private Long writeTs;

    public MypageNoticeListOutVo(P_MypageNoticeSelectVo target) {
        setNoticeIdx(target.getNoticeIdx());
        setTitle(target.getTitle());
        setContents(target.getContents());
        setTop(target.getTopFix() == 1 ? true : false);
        setWriteDt(DalbitUtil.getUTCFormat(target.getWriteDate()));
        setWriteTs(DalbitUtil.getUTCTimeStamp(target.getWriteDate()));
    }
}

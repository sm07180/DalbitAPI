package com.dalbit.main.vo.procedure;

import com.dalbit.main.vo.request.NoticeDetailVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_NoticeDetailVo {

    private int noticeIdx;

    public P_NoticeDetailVo(){}
    public P_NoticeDetailVo(NoticeDetailVo noticeDetailVo){
        setNoticeIdx(noticeDetailVo.getNoticeIdx());
    }
}

package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class BroadcastNoticeDelVo {
    @NotNull
    private Long roomNoticeNo;
    @NotNull
    private Long memNo;
    private Long roomNo;

    public BroadcastNoticeDelVo(Long roomNoticeNo, Long memNo, Long roomNo) {
        this.roomNoticeNo = roomNoticeNo;
        this.memNo = memNo;
        this.roomNo = roomNo;
    }
}

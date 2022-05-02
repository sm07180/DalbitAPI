package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class BroadcastNoticeAddVo {
    private Integer memNo;
    private Integer roomNo;

    @NotNull
    private String notice;

    public BroadcastNoticeAddVo(Integer memNo, Integer roomNo, String notice) {
        this.memNo = memNo;
        this.roomNo = roomNo;
        this.notice = notice;
    }
}

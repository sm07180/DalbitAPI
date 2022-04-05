package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class BroadcastNoticeUpdVo {
    private Long roomNoticeNo;
    private String memNo;
    private String roomNo;
    @NotNull
    private String notice;
}

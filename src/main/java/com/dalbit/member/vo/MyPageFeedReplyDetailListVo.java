package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MyPageFeedReplyDetailListVo {
    @NotNull
    private Long regNo;
    @NotNull
    private Long tailNo;
}

package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MyPageFeedDetailListVo {
    @NotNull
    private Integer feedNo;
    @NotNull
    private String memNo;
    @NotNull
    private Long viewMemNo;

}

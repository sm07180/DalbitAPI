package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class ProfileFeedFixSelVo {
    @NotNull
    private String memNo;
    @NotNull
    private Integer pageNo;
    @NotNull
    private Integer pageCnt;
}

package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MyPageFeedListVo {
    private String memNo;
    private Integer pageNo;
    private Integer pageCnt;
}

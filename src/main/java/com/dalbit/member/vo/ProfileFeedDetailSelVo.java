package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileFeedDetailSelVo {
    @NotNull
    private Integer noticeNo;
    @NotNull
    private String memNo;
}

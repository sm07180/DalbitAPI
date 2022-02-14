package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ProfileFeedSelVo {
    @NotNull
    private String memNo;
    @NotNull
    private Integer pageNo;
    @NotNull
    private Integer pagePerCnt;
}

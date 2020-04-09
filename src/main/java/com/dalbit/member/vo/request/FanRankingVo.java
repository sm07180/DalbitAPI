package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class FanRankingVo {

    @NotBlank
    private String memNo;
    private int page;
    private int records;

}

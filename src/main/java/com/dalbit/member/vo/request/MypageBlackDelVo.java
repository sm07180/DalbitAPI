package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MypageBlackDelVo {

    @NotBlank
    private String memNo;
}

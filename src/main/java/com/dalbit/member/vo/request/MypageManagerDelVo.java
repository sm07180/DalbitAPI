package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MypageManagerDelVo {

    @NotBlank
    private String memNo;
}

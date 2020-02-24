package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter @Setter
public class MypageNoticeAddVo {

    @NotBlank
    private String memNo;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;

    private String topFix;
}

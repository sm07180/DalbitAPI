package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageNoticeEditVo {

    @NotBlank
    private String memNo;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;
    @NotNull
    private Integer noticeIdx;

    private String topFix;
}

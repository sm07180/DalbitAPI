package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class MypageNoticeEditVo {

    @NotBlank
    private String memNo;
    @NotBlank @Size(max = 20)
    private String title;
    @NotBlank @Size(max = 1024)
    private String contents;
    @NotNull
    private Integer noticeIdx;

    private String isTop;
}

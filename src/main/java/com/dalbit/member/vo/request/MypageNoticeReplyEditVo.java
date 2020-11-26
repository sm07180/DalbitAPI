package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageNoticeReplyEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"공지댓글번호를\"}")
    private Integer replyIdx;

    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    private String contents;

}

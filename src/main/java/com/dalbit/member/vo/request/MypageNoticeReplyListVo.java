package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageNoticeReplyListVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Integer noticeIdx;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;
}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"댓글번호를\"}")
    private Integer replyIdx;

    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message = "{\"ko_KR\" : \"내용을\"}", max = 250)
    private String contents;
}

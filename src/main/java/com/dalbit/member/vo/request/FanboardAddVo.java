package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class FanboardAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"댓글 구분을\"}")
    @Min(value = 1, message = "댓글 구분을")
    @Max(value = 2, message = "댓글 구분을")
    private Integer depth;

    private Integer parentGroupIdx;

    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message = "{\"ko_KR\" : \"내용을\"}", max = 250)
    private String contents;

    @NotNull(message = "{\"ko_KR\" : \"비밀글 여부를\"}")
    @Min(value = 0, message = "비밀글 여부를")
    @Max(value = 1, message = "비밀글 여부를")
    private Integer viewOn;


}

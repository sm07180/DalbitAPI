package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ClipDeclarVo {

    @NotBlank(message = "{\"ko_KR\" : \"클립 회원번호을\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 회원번호을\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"신고 사유를\"}")
    private Integer reason;

    @Size (message = "{\"ko_KR\" : \"상세 사유를\"}", max = 256)
    private String cont;

    @NotBlank(message = "{\"ko_KR\" : \"클립번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립번호를\"}")
    private String clipNo;
}

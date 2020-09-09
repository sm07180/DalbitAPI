package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipUploadListVo {

    @NotBlank(message = "{\"ko_KR\" : \"스타 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"스타 회원번호를\"}")
    private String memNo;

    private Integer page;
    private Integer records;
}

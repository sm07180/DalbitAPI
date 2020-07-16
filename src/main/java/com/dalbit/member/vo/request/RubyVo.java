package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RubyVo {

    @NotBlank(message = "{\"ko_KR\" : \"선물 할 회원을\"}")
    @NotNull(message = "{\"ko_KR\" : \"선물 할 회원을\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"달을\"}")
    @Min(message = "{\"ko_KR\" : \"달을\"}", value = 10)
    private int dal;
}

package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ErrorLogVo {

    @NotBlank(message = "{\"ko_KR\" : \"데이터타입을\"}")
    @NotNull(message = "{\"ko_KR\" : \"데이터타입을\"}")
    private String dataType;
    @NotBlank(message = "{\"ko_KR\" : \"커맨드타입을\"}")
    @NotNull(message = "{\"ko_KR\" : \"커맨드타입을\"}")
    private String commandType;
    @NotBlank(message = "{\"ko_KR\" : \"상세내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"상세내용을\"}")
    private String desc;
}

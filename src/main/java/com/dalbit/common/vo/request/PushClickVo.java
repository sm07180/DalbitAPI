package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class PushClickVo {

    @NotBlank(message = "{\"ko_KR\" : \"푸시번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"푸시번호를\"}")
    private String pushIdx;
    private String mem_no;

}

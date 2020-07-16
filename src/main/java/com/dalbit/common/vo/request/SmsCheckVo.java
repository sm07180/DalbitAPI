package com.dalbit.common.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class SmsCheckVo {

    @NotNull(message = "{\"ko_KR\" : \"인증코드를\"}")
    private Integer CMID;

    @NotNull(message = "{\"ko_KR\" : \"인증번호를\"}")
    private Integer code;
}

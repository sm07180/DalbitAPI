package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RoulettePhoneVo {
    @NotBlank(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"휴대폰 번호를\"}")
    private String phone;

    @NotNull(message = "{\"ko_KR\" : \"당첨지정번호를\"}")
    private int winIdx;
}

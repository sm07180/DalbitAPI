package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ExchangeAccountDeleteVo {

    @NotNull(message = "{\"ko_KR\" : \"고유번호를\"}")
    private Integer idx;

    @NotBlank(message = "{\"ko_KR\" : \"이전 계좌번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"이전 계좌번호를\"}")
    private String beforeAccountNo;

}

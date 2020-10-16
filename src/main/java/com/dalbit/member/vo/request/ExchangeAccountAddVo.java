package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ExchangeAccountAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"예금주명을\"}")
    @NotNull(message = "{\"ko_KR\" : \"예금주명을\"}")
    private String accountName;

    @NotBlank(message = "{\"ko_KR\" : \"계좌번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"계좌번호를\"}")
    private String accountNo;

    @NotBlank(message = "{\"ko_KR\" : \"은행코드를\"}")
    @NotNull(message = "{\"ko_KR\" : \"은행코드를\"}")
    private String bankCode;

    @NotBlank(message = "{\"ko_KR\" : \"은행명을\"}")
    @NotNull(message = "{\"ko_KR\" : \"은행명을\"}")
    private String bankName;

}

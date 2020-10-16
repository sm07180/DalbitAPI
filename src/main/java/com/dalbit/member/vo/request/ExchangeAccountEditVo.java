package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ExchangeAccountEditVo {

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

    @NotNull(message = "{\"ko_KR\" : \"고유번호를\"}")
    private Integer idx;

    @NotBlank(message = "{\"ko_KR\" : \"이전 계좌번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"이전 계좌번호를\"}")
    private String beforeAccountNo;

}

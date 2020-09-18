package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class WalletListVo {
    @NotNull(message = "{\"ko_KR\" : \"지갑 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"지갑 구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"지갑 구분을\"}", value = 1)
    private Integer walletType;

    @NotBlank(message = "{\"ko_KR\" : \"지갑 코드를\"}")
    @NotNull(message = "{\"ko_KR\" : \"지갑 코드를\"}")
    private String walletCode;

    private Integer page;
    private Integer records;
}

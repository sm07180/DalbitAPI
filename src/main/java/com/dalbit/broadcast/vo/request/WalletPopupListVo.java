package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class WalletPopupListVo {

    @NotNull(message = "{\"ko_KR\" : \"지갑 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"지갑 구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"지갑 구분을\"}", value = 1)
    private Integer walletType;

}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ByeolVo {

    @NotNull(message = "{\"ko_KR\" : \"구분을\"}")
    @Min(message = "{\"ko_KR\" : \"구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"구분을\"}", value = 3)
    private Integer walletType;
    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회건수를\"}", value = 1)
    private Integer records;
}

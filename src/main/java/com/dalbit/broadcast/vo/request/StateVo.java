package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class StateVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"마이크 상태를\"}")
    @NotNull(message = "{\"ko_KR\" : \"마이크 상태를\"}")
    private String isMic;

    @NotBlank(message = "{\"ko_KR\" : \"통화 상태를\"}")
    @NotNull(message = "{\"ko_KR\" : \"통화 상태를\"}")
    private String isCall;

    @NotBlank(message = "{\"ko_KR\" : \"미디어 상태를\"}")
    @NotNull(message = "{\"ko_KR\" : \"미디어 상태를\"}")
    private String isAnt;

    private Integer state;
}

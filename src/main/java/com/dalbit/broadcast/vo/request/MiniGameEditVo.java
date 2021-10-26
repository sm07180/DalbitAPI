package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class MiniGameEditVo {

    private String isFree;

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @Max(message = "{\"ko_KR\" : \"금액을\"}", value = 1000)
    private int payAmt;

    private int optCnt = 2;
    private String optList;

    @NotNull(message = "{\"ko_KR\" : \"미니게임 번호를\"}")
    private Integer gameNo;

    private String autoYn;
}

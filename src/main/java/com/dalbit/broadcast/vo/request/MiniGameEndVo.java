package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class MiniGameEndVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotNull(message = "{\"ko_KR\" : \"미니게임 번호를\"}")
    private Integer gameNo;

}

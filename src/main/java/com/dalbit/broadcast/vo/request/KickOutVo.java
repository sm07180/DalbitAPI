package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class KickOutVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"강퇴할 회원 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"강퇴할 회원 번호를\"}")
    private String blockNo;
}

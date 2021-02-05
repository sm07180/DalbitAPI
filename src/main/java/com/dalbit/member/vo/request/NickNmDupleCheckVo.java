package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class NickNmDupleCheckVo {

    @NotBlank(message = "{\"ko_KR\" : \"닉네임을\"}")
    @NotNull(message = "{\"ko_KR\" : \"닉네임을\"}")
    @Size(message = "{\"ko_KR\" : \"닉네임을\"}", min = 2)
    private String nickNm;
}

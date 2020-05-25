package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class NickNmDupleCheckVo {

    @NotBlank @Size(min = 2, max = 20)
    private String nickNm;
}

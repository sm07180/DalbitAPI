package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class NickNmDupleCheckValidaionVo {

    @NotBlank @Size(max = 20)
    private String nickNm;
}

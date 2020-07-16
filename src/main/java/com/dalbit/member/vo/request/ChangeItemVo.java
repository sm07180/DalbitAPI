package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ChangeItemVo {

    @NotBlank(message = "{\"ko_KR\" : \"아이템을\"}")
    @NotNull(message = "{\"ko_KR\" : \"아이템을\"}")
    private String itemCode;

}

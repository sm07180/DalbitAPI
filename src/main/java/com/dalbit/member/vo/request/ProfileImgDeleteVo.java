package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ProfileImgDeleteVo {

    @NotNull(message = "{\"ko_KR\" : \"고유번호를\"}")
    private Integer idx;
}

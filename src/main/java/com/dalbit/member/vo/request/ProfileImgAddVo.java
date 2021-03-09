package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ProfileImgAddVo {

    @NotBlank(message = "{\"ko_KR\" : \"이미지를\"}")
    @NotNull(message = "{\"ko_KR\" : \"이미지를\"}")
    private String profImg;
}

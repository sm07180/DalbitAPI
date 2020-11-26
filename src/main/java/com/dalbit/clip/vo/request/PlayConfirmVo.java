package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class PlayConfirmVo {

    @NotBlank(message = "{\"ko_KR\" : \"클립번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립번호를\"}")
    private String clipNo;

    @NotNull(message = "{\"ko_KR\" : \"재생확인번호를\"}")
    private Integer playIdx;
}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class AutoChangeSettingEditVo {

    @NotNull(message = "{\"ko_KR\" : \"변경 값을\"}")
    private String autoChange;
}
package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class BroadcastOptionAddVo {

    @NotNull(message = "{\"ko_KR\" : \"옵션타입을\"}")
    @Min(message = "{\"ko_KR\" : \"옵션타입을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"옵션타입을\"}", value = 2)
    private Integer optionType;

    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    private String contents;
}

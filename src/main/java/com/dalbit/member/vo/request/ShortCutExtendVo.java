package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class ShortCutExtendVo {

    @NotNull(message = "{\"ko_KR\" : \"빠른말 번호를\"}")
    private Integer orderNo;
    @NotBlank(message = "{\"ko_KR\" : \"명령을\"}")
    @NotNull(message = "{\"ko_KR\" : \"명령을\"}")
    @Size(message = "{\"ko_KR\" : \"명령을\"}", max = 4)
    private String order;
    @NotBlank(message = "{\"ko_KR\" : \"빠른말을\"}")
    @NotNull(message = "{\"ko_KR\" : \"빠른말을\"}")
    @Size (message = "{\"ko_KR\" : \"빠른말을\"}", max = 200)
    private String text;

}

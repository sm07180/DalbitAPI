package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class Apply004Vo {
    private Integer eventIdx = 4;
    @NotBlank(message = "{\"ko_KR\" : \"성명을\"}")
    @Size(message = "{\"ko_KR\" : \"성명을\"}", max = 10)
    private String name;
    @NotBlank(message = "{\"ko_KR\" : \"연락처를\"}")
    @Size(message = "{\"ko_KR\" : \"연락처를\"}", max = 12)
    private String contactNo;
    @NotBlank(message = "{\"ko_KR\" : \"방송소개를\"}")
    @Size(message = "{\"ko_KR\" : \"방송소개를\"}", max = 1000)
    private String introduce;
}

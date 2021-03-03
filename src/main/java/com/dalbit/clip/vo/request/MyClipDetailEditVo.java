package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class MyClipDetailEditVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotNull(message = "{\"ko_KR\" : \"공개여부를\"}")
    @Min(message = "{\"ko_KR\" : \"공개여부를\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"공개여부를\"}", value = 1)
    private Integer openType;

}

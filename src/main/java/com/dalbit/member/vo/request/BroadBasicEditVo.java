package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class BroadBasicEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"주제를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주제를\"}")
    @Size(message = "{\"ko_KR\" : \"주제를\"}", min = 2, max = 2)
    private String roomType;

    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size(message = "{\"ko_KR\" : \"제목을\"}", min = 2)
    private String title;

    private String bgImg;
    private String bgImgDel;
    private String bgImgRacy;

    @Size(message = "{\"ko_KR\" : \"인사말을\"}")
    private String welcomMsg;

    private String notice;

    @NotNull(message = "{\"ko_KR\" : \"입장제한을\"}")
    @Min(message = "{\"ko_KR\" : \"입장제한을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"입장제한을\"}", value = 2)
    private int entryType;
}

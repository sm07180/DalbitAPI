package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class RoomEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"주제를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주제를\"}")
    @Size(message = "{\"ko_KR\" : \"주제를\"}", min = 2, max = 2)
    private String roomType;

    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size(message = "{\"ko_KR\" : \"제목을\"}", min = 2, max = 20)
    private String title;

    private String bgImg;
    private String bgImgDel;
    private String bgImgRacy;

    @Size(message = "{\"ko_KR\" : \"인사말을\"}", max = 100)
    private String welcomMsg;

}

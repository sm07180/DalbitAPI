package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class RoomCreateVo {

    @NotBlank(message = "{\"ko_KR\" : \"주제를\"}")
    @NotNull(message = "{\"ko_KR\" : \"주제를\"}")
    @Size(message = "{\"ko_KR\" : \"주제를\"}", min = 2, max = 2)
    private String roomType;

    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size(message = "{\"ko_KR\" : \"제목을\"}", min = 2, max = 20)
    private String title;

    private String bgImg;

    private String bgImgRacy;

    @Size(message = "{\"ko_KR\" : \"인사말을\"}", max = 100)
    private String welcomMsg;

    private String notice;

    @NotNull(message = "{\"ko_KR\" : \"입장제한을\"}")
    @Min(message = "{\"ko_KR\" : \"입장제한을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"입장제한을\"}", value = 2)
    private Integer entryType;

    private String djListenerIn = "1";
    private String djListenerOut = "0";
}

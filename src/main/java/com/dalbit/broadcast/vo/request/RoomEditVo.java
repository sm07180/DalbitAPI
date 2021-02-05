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
    @Size(message = "{\"ko_KR\" : \"제목을\"}", min = 2)
    private String title;

    private String bgImg;
    private String bgImgDel;
    private String bgImgRacy;

    @Size(message = "{\"ko_KR\" : \"인사말을\"}")
    private String welcomMsg;

    private String djListenerIn = "1";
    private String djListenerOut = "0";

    private Integer imageType=1;                  //스페셜DJ일 경우 실시간live 이미지 노출선택(1:프로필, 2:배경)
}

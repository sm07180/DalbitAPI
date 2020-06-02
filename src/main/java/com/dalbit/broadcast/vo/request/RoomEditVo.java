package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class RoomEditVo {

    @NotBlank
    private String roomNo;

    @NotBlank @Size(min = 2, max = 2)
    private String roomType;

    @NotBlank @Size(min = 2, max = 20)
    private String title;

    private String bgImg;
    private String bgImgDel;
    private String bgImgRacy;
    private String welcomMsg;

}

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

    @NotBlank
    private String title;

    private String bgImg;
    private String bgImgDel;

    @Max(5)
    private Integer bgImgRacy;

    private String welcomMsg;

}

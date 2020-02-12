package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class RoomCreateVo {

    @NotBlank @Size(min = 2, max = 2)
    private String roomType;

    @NotBlank
    private String title;

    private String bgImg;

    @Max(5)
    private Integer bgImgRacy;

    private String welcomMsg;

    private String notice;

    @NotNull @Min(0) @Max(2)
    private Integer entryType;



}

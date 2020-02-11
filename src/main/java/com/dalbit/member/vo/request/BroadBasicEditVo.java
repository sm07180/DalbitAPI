package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BroadBasicEditVo {

    @NotBlank
    private String roomType;
    @NotBlank
    private String title;
    private String bgImg;
    private String bgImgDel;
    private int bgImgRacy;
    @NotBlank
    private String wecomMsg;
    private String notice;
    @NotBlank
    private int entryType;
}

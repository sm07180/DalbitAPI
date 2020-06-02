package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BroadBasicEditVo {

    @NotBlank @Size(min = 2, max = 2)
    private String roomType;
    @NotBlank @Size(max = 100)
    private String title;
    private String bgImg;
    private String bgImgDel;
    private String bgImgRacy;
    @Size (max = 200)
    private String welcomMsg;
    private String notice;
    @NotBlank
    private int entryType;
}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileEditVo {

    @Size(max = 1)
    private String gender;

    @NotBlank @Size(max = 20)
    private String nickNm;

    @NotBlank @Size(min = 8, max = 8)
    private String birth;

    private String name;
    private String profImgRacy;
    private String profImg;
    private String profImgDel;
    @Size(max = 100)
    private String profMsg;

}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileEditVo {

    @Size(message = "{\"ko_KR\" : \"성별을\"}", max = 1)
    private String gender;

    @Size(message = "{\"ko_KR\" : \"닉네임을\"}", min = 2, max = 20)
    private String nickNm;

    @Size(message = "{\"ko_KR\" : \"생년월일을\"}", min = 8, max = 8)
    private String birth;

    //private String name;
    private String profImgRacy;
    private String profImg;
    private String profImgDel;

    @Size(message = "{\"ko_KR\" : \"프로필 메세지를\"}", max = 100)
    private String profMsg;

}

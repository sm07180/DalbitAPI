package com.dalbit.member.vo.request;

import com.dalbit.validator.annotation.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class JoinValidationVo {

    @NotBlank @Size(max = 1)
    private String memType;

    @NotBlank @Size(max = 25)
    private String memId;

    @Password
    private String memPwd;

    @NotBlank @Size(max = 1)
    private String gender;

    @NotBlank @Size(max = 20)
    private String nickNm;

    @NotBlank @Size(min = 8, max = 8)
    private String birth;

    @NotBlank @Size(max = 1)
    private String term1, term2, term3, term4, term5;

    @NotBlank
    private String name;

    private String profImg;
    private int profImgRacy;
    private String email;
}

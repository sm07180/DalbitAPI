package com.dalbit.member.vo.request;

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

    @Size(max = 20/*, message = "8자~20자, 영문,숫자,특수문자 중 2가지 이상 조합 확인"*/)
    private String memPwd;

    @NotBlank @Size(max = 1)
    private String gender;

    @NotBlank @Size(max = 20)
    private String nickNm;

    @NotBlank @Size(min = 8, max = 8)
    private String birth;

    @NotBlank @Size(max = 1)
    private String term1, term2, term3;

    @NotBlank
    private String name;

    private String profImg;
    private String profImgRacy;
    private String email;
}

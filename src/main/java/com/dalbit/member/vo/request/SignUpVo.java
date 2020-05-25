package com.dalbit.member.vo.request;

import com.dalbit.validator.annotation.Password;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpVo {

    @NotBlank @Size(max = 1)
    private String memType;

    @NotBlank @Size(max = 50)
    private String memId;

    @Password
    private String memPwd;

    @Size(max = 1)
    private String gender;

    @NotBlank @Size(min = 2, max = 20)
    private String nickNm;

    @NotBlank @Size(min = 8, max = 8)
    private String birth;

    @NotBlank @Size(max = 1)
    private String term1, term2, term3, term4, term5;

    private String name;
    private String profImg;
    private String profImgRacy;
    private String email;
}

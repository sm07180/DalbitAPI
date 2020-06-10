package com.dalbit.member.vo.request;

import com.dalbit.validator.annotation.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePwVo {

    @NotBlank
    @Size(max = 25)
    private String memId;

    @Password
    @Size(max = 20)
    private String memPwd;
}

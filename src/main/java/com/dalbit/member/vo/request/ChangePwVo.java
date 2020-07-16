package com.dalbit.member.vo.request;

import com.dalbit.validator.annotation.Password;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ChangePwVo {

    @NotBlank(message = "{\"ko_KR\" : \"아이디를 \"}")
    @NotNull(message = "{\"ko_KR\" : \"아이디를\"}")
    @Size(message = "{\"ko_KR\" : \"아이디를\"}", max = 25)
    private String memId;

    @Password
    private String memPwd;
}

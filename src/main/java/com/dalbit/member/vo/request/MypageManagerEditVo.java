package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageManagerEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"수정 할 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"수정 할 회원번호를\"}")
    private String memNo;

    @NotBlank(message = "{\"ko_KR\" : \"권한을\"}")
    @NotNull(message = "{\"ko_KR\" : \"권한을\"}")
    private String role;
}

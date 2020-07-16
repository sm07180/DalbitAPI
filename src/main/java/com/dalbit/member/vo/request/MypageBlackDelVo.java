package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MypageBlackDelVo {

    @NotBlank(message = "{\"ko_KR\" : \"해제 할 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"해제 할 회원번호를\"}")
    private String memNo;
}

package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FanstartInsertVo {

    @NotBlank(message = "{\"ko_KR\" : \"등록 할 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"등록 할 회원번호를\"}")
    private String memNo;

    private int type=0;   // 0:일반, 1:추천DJ
}

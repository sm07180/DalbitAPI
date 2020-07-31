package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class WithdrawalVo {

    @NotBlank(message = "{\"ko_KR\" : \"UID를\"}")
    @NotNull(message = "{\"ko_KR\" : \"UID를\"}")
    private String uid;

}
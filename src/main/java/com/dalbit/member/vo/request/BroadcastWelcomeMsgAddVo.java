package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class BroadcastWelcomeMsgAddVo {
    @NotBlank(message = "{\"ko_KR\" : \"인사말을\"}")
    @NotNull(message = "{\"ko_KR\" : \"인사말을\"}")
    @Size (message = "{\"ko_KR\" : \"인사말을\"}")
    private String welcomeMsg;
}

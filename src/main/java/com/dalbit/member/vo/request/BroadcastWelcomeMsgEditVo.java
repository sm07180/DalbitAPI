package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class BroadcastWelcomeMsgEditVo {
    @NotNull(message = "{\"ko_KR\" : \"인사말 번호를\"}")
    private Integer welcomeMsgIdx;

    @NotBlank(message = "{\"ko_KR\" : \"인사말\"}")
    @NotNull(message = "{\"ko_KR\" : \"인사말\"}")
    @Size (message = "{\"ko_KR\" : \"인사말\"}", max = 20)
    private String welcomeMsg;
}

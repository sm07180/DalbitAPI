package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class BroadcastWelcomeMsgDeleteVo {
    @NotNull(message = "{\"ko_KR\" : \"인사말 번호를\"}")
    private Integer welcomeMsgIdx;
}

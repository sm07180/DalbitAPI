package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RoomMemberInfoVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;
    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;
}

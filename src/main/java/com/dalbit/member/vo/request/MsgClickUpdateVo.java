package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class MsgClickUpdateVo {

    @Min(message = "{\"ko_KR\" : \"메세지 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"메세지 구분을\"}", value = 2)
    private int msgSlct;        //1: 퀵메시지, 2:인싸티콘

    @NotNull(message = "{\"ko_KR\" : \"메세지 번호를\"}")
    private Integer msgIdx;     //메시지 고유번호
}

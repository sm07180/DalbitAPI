package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class MyPageFeedReplyUpdVo {
    @NotNull
    private Integer tailNo;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    @NotNull
    private String tmemConts;
}

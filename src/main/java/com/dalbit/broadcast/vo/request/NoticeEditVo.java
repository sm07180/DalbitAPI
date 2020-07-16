package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class NoticeEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"공지사항을\"}")
    @NotNull(message = "{\"ko_KR\" : \"공지사항을\"}")
    @Size(message = "{\"ko_KR\" : \"공지사항을\"}", max = 200)
    private String notice;
}

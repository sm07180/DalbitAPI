package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class NoticeDetailVo {

    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    @Min(message = "{\"ko_KR\" : \"공지번호를\"}", value = 0)
    private int noticeIdx;
}

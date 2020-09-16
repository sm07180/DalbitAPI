package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipReplyListVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;
}

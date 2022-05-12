package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class StoryViewVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value =  1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records;

    private String plusYn = ""; // 사연 플러스 여부 ["y": 사연플러스, "n": 일반사연, "": 전체 조회]
}

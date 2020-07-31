package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class FanListNewVo {

    @NotBlank(message = "{\"ko_KR\" : \"팬 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"팬 회원번호를\"}")
    private String memNo;
    private int page;
    private int records;

    @NotNull(message = "{\"ko_KR\" : \"정렬조건을\"}")
    @Min(message = "{\"ko_KR\" : \"정렬조건을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"정렬조건을\"}", value = 3)
    private Integer sortType;
}

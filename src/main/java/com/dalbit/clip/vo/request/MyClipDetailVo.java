package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class MyClipDetailVo {
    @NotNull(message = "{\"ko_KR\" : \"검색 구분을\"}")
    @Min(message = "{\"ko_KR\" : \"검색 구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"검색 구분을\"}", value = 4)
    private Integer myClipType;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건 수를\"}", value = 1)
    private Integer records;
}

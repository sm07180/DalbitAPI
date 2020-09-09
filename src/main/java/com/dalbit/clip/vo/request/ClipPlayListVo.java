package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipPlayListVo {

    @NotNull(message = "{\"ko_KR\" : \"정렬방식을\"}")
    @Min(message = "{\"ko_KR\" : \"정렬방식을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"정렬방식을\"}", value = 5)
    private Integer sortType;

    private Integer page;
    private Integer records;
}

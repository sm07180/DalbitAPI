package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class ClipListVo {

    @NotNull(message = "{\"ko_KR\" : \"검색구분을\"}")
    @Min(message = "{\"ko_KR\" : \"검색구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"검색구분을\"}", value = 4)
    private Integer slctType;

    @NotNull(message = "{\"ko_KR\" : \"기간 선택을\"}")
    @Min(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"기간 선택을\"}", value = 2)
    private Integer dateType=2;

    /*@NotNull(message = "{\"ko_KR\" : \"신입구분을\"}")
    @Min(message = "{\"ko_KR\" : \"신입구분을\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"신입구분을\"}", value = 1)*/
    private Integer djType = 0;

    private String subjectType;
    private String gender;
    private Integer page;
    private Integer records;

    @Size(message = "{\"ko_KR\" : \"검색어를\"}", max = 50)
    private String search;
}

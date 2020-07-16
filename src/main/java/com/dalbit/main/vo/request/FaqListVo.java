package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FaqListVo {

    @NotNull(message = "{\"ko_KR\" : \"FAQ구분를\"}")
    @Min(message = "{\"ko_KR\" : \"FAQ구분를\"}", value = 0)
    @Max(message = "{\"ko_KR\" : \"FAQ구분를\"}", value = 5)
    private Integer faqType;
    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회건수를\"}", value = 1)
    private Integer records;
    /*@NotNull
    @Min(0) @Max(2)*/
    private Integer searchType;
    private String searchText;
}

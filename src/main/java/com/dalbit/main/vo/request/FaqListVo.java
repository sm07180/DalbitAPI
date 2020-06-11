package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FaqListVo {

    @NotNull
    @Min(0) @Max(5)
    private Integer faqType;
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
    @NotNull
    @Min(0) @Max(2)
    private Integer searchType;
    private String searchText;
}

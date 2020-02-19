package com.dalbit.search.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class SearchVo {

    @NotBlank @Size(min = 2)
    private String searchText;
    private Integer page;
    private Integer records;
}

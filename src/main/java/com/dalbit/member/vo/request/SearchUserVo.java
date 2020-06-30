package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class SearchUserVo {
    @NotNull
    @Min(0) @Max(2)
    private Integer userType;
    @NotBlank
    private String search;
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
    private String searchType;
}

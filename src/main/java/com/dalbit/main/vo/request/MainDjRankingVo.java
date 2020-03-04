package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class MainDjRankingVo {

    @NotNull
    @Min(1) @Max(3)
    private Integer rankType;
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;


}
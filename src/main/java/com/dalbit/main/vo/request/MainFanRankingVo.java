package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class MainFanRankingVo {

    @NotNull
    @Min(0) @Max(3)
    private Integer rankType;
    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;


}

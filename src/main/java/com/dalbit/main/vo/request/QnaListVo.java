package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class QnaListVo {

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}

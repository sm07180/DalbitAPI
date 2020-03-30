package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter @Setter
public class MainMyDjVo {

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}

package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class StoryHistoryVo {
    @NotNull
    @Min(1)
    private Integer pageNo;

    @NotNull
    @Min(1)
    private Integer pagePerCnt;
}

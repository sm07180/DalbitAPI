package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class NoticeDetailVo {

    @NotNull @Min(0)
    private int noticeIdx;
}

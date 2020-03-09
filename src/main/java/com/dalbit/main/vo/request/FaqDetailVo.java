package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class FaqDetailVo {

    @NotNull
    private int faqIdx;
}

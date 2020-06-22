package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class QnaDelVo {

    @NotNull
    private Integer qnaIdx;

}

package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
public class QnaDelVo {

    @NotNull(message = "{\"ko_KR\" : \"문의 번호를\"}")
    private Integer qnaIdx;

}

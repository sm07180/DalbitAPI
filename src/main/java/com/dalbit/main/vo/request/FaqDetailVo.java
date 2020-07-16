package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class FaqDetailVo {

    @NotNull(message = "{\"ko_KR\" : \"FAQ 번호를\"}")
    private int faqIdx;
}

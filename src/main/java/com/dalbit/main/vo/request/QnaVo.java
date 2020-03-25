package com.dalbit.main.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class QnaVo {

    @Min(1) @Max(99)
    private int qnaType;
    @NotBlank
    private String title;
    @NotBlank
    private String contents;

    private String questionFile;
    @NotBlank
    private String email;
}

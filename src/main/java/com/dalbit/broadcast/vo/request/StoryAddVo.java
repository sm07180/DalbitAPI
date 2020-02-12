package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class StoryAddVo {

    @NotBlank
    private String roomNo;

    @NotBlank
    private String contents;
}

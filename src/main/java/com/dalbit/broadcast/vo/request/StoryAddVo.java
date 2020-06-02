package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter @Setter
public class StoryAddVo {

    @NotBlank
    private String roomNo;

    @NotBlank @Size(max = 500)
    private String contents;
}

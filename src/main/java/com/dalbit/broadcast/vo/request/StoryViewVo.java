package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class StoryViewVo {

    @NotBlank
    private String roomNo;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}

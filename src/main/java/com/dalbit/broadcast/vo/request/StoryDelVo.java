package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class StoryDelVo {

    @NotBlank
    private String roomNo;
    @NotNull
    private Integer storyIdx;

    private Integer page;
    private Integer records;
}

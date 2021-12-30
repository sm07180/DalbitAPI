package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class MoonLandScoreInsVO {
    @NotNull()
    Integer type = 0;
    @NotNull()
    Integer score = 0;
    @NotNull()
    @NotEmpty()
    @NotBlank()
    String roomNo = "";
}

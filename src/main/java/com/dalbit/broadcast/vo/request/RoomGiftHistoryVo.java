package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter @Setter
public class RoomGiftHistoryVo {

    @NotBlank
    private String roomNo;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;
}

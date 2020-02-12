package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter @Setter
public class RoomListVo {

    @Size(max = 2)
    private String roomType;

    @Min(1)
    private Integer page;
    @Min(1)
    private Integer records;

}

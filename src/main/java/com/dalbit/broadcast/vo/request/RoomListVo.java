package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter @Setter
public class RoomListVo {

    @Size(message = "{\"ko_KR\" : \"주제를\"}", max = 2)
    private String roomType;

    @Min(message = "{\"ko_KR\" : \"페이지를\"}", value = 1)
    private Integer page;
    @Min(message = "{\"ko_KR\" : \"조회 건수를\"}", value = 1)
    private Integer records = 50;

    private Integer searchType = 1;

    private String gender;

    @Size(message = "{\"ko_KR\" : \"검색어를\"}", max = 50)
    private String search;  //

    private String mediaType;

    private Integer djType;
}

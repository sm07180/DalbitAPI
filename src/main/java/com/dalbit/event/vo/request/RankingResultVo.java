package com.dalbit.event.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RankingResultVo {

    private String memNo;                  //회원번호

    @NotNull(message = "{\"ko_KR\" : \"랭킹구분을\"}")
    @Min(message = "{\"ko_KR\" : \"랭킹구분을\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"랭킹구분을\"}", value = 3)
    private Integer slctType;              //랭킹구분(1: 경험치, 2: 좋아요, 3: 선물 )
    @NotNull(message = "{\"ko_KR\" : \"회차를\"}")
    @Min(message = "{\"ko_KR\" : \"회차를\"}", value = 1)
    @Max(message = "{\"ko_KR\" : \"회차를\"}", value = 3)
    private Integer round;                  //회차(1, 2, 3 )
}

package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class BoosterVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;
    @NotBlank(message = "{\"ko_KR\" : \"선물 아이템을\"}")
    @NotNull(message = "{\"ko_KR\" : \"선물 아이템을\"}")
    private String itemNo;
    @NotNull(message = "{\"ko_KR\" : \"선물 갯수를\"}")
    private Integer itemCnt;

    private String isItemUse="";
}

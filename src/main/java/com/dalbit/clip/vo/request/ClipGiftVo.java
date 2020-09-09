package com.dalbit.clip.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class ClipGiftVo {
    @NotBlank(message = "{\"ko_KR\" : \"클립 번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"클립 번호를\"}")
    private String clipNo;

    @NotBlank(message = "{\"ko_KR\" : \"선물받을 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"선물받을 회원번호를\"}")
    private String memNo;

    @NotBlank(message = "{\"ko_KR\" : \"아이템코드를\"}")
    @NotNull(message = "{\"ko_KR\" : \"아이템코드를\"}")
    private String itemCode;

    @NotNull(message = "{\"ko_KR\" : \"아이템 개수를\"}")
    @Min(message = "{\"ko_KR\" : \"아이템 개수를\"}", value = 1)
    private Integer itemCnt;
}

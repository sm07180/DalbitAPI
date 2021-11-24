package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter @Setter
public class GiftVo {

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    @NotBlank(message = "{\"ko_KR\" : \"선물 받은 회원을\"}")
    @NotNull(message = "{\"ko_KR\" : \"선물 받은 회원을\"}")
    private String memNo;

    @NotBlank(message = "{\"ko_KR\" : \"선물 아이템을\"}")
    @NotNull(message = "{\"ko_KR\" : \"선물 아이템을\"}")
    private String itemNo;

    @NotNull(message = "{\"ko_KR\" : \"선물 갯수를\"}")
    @Min(message = "{\"ko_KR\" : \"선물 갯수를\"}", value = 1)
    private Integer itemCnt;

    @NotBlank(message = "{\"ko_KR\" : \"비밀 선물 여부를\"}")
    @NotNull(message = "{\"ko_KR\" : \"비밀 선물 여부를\"}")
    private String isSecret;

    private String ttsText; // tts 내용
    private String actorId;    // tts 배우 id
}

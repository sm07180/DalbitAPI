package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
public class MyPageFeedUpdVo {
    @NotNull
    private Integer feedNo;
    @NotNull
    private Long memNo;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    @NotNull
    private String feedContents;

    private List<MyPageFeedPictureOutVo> photoInfoList = null;
    private String delChrgrName = "";
}

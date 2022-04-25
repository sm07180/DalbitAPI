package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter @Setter
public class MyPageFeedAddVo {
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size(message="{\"ko_KR\" : \"내용을\"}", max = 1000)
    private String feedContents;
    private Long memNo;
    private List<MyPageFeedPictureOutVo> photoInfoList = null;
}

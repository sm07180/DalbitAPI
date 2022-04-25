package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ProfileFeedAddVo {
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size (message="{\"ko_KR\" : \"내용을\"}", max = 1000)
    private String contents;

    @NotNull
    private Integer topFix;

    private String imgName;

    private List<ProfileFeedPhotoOutVo> photoInfoList = null;
    private String title;
}

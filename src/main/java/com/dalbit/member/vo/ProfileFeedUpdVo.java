package com.dalbit.member.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class ProfileFeedUpdVo {
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size (message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    @NotNull
    private String contents;
    @NotNull
    private Long noticeIdx;
    @NotNull
    private Integer topFix;

    @Builder.Default
    private List<ProfileFeedPhotoOutVo> photoInfoList = null;
    @Builder.Default
    private String chrgrName = "";        // 수정한 유저명
    private String title;
}

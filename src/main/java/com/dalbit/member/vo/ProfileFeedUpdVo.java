package com.dalbit.member.vo;

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
    private String noticeContents;
    @NotNull
    private Long noticeNo;
    @NotNull
    private Integer noticeTopFix;

    private String ImgName;

    private List<ProfileFeedPhotoOutVo> photoInfoList = null;
    private String chrgrName = "";        // 수정한 유저명
    private String noticeTitle;
}

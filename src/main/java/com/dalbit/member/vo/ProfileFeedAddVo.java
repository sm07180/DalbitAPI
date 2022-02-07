package com.dalbit.member.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ProfileFeedAddVo {
    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size(message = "{\"ko_KR\" : \"제목을\"}", max = 20)
    private String title;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size (message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    private String contents;

    @NotNull
    private Integer topFix;
    @Builder.Default
    private String imagePath = "";
}

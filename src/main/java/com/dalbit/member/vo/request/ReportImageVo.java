package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter @ToString
public class ReportImageVo {
    @NotBlank(message = "{\"ko_KR\" : \"신고대상 회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"신고대상 회원번호를\"}")
    private String memNo;

    @NotNull(message = "{\"ko_KR\" : \"이미지 신고구분을\"}")
    private Integer imageType;

    @NotBlank(message = "{\"ko_KR\" : \"이미지번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"이미지번호를\"}")
    private String imageIdx;

    @NotBlank(message = "{\"ko_KR\" : \"이미지경로를\"}")
    @NotNull(message = "{\"ko_KR\" : \"이미지경로를\"}")
    private String imagePath;

    @NotBlank(message = "{\"ko_KR\" : \"방번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"방번호를\"}")
    private String roomNo;

    private int reason=0;   //신고사유(1:프로필사진, 2:음란성, 3:광고 및 상업성, 4:욕설 및 비방성, 5: 기타)
    private String etc="";  //기타 사유
}

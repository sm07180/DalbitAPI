package com.dalbit.member.vo.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter @Setter
public class MypageNoticeEditVo {

    @NotBlank(message = "{\"ko_KR\" : \"회원번호를\"}")
    @NotNull(message = "{\"ko_KR\" : \"회원번호를\"}")
    private String memNo;
    @NotBlank(message = "{\"ko_KR\" : \"제목을\"}")
    @NotNull(message = "{\"ko_KR\" : \"제목을\"}")
    @Size (message = "{\"ko_KR\" : \"제목을\"}", max = 20)
    private String title;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    @Size (message = "{\"ko_KR\" : \"내용을\"}", max = 1024)
    private String contents;
    @NotNull(message = "{\"ko_KR\" : \"공지번호를\"}")
    private Integer noticeIdx;

    private String isTop;
    private String imagePath;
}

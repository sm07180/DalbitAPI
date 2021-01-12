package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class PhotoEventInputVo {

    private int page = 1;
    private int records = 50;
    private int slct_type = 0;

    private int idx;
    private int event_idx;
    private int event_member_idx;
    private String mem_no;
    @NotBlank(message = "{\"ko_KR\" : \"첨부파일 이미지를\"}")
    @NotNull(message = "{\"ko_KR\" : \"첨부파일 이미지를\"}")
    private String image_url;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    private String contents;
    private int del_yn;
    private int totalCnt = 0;
}

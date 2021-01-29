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
    private String mem_name;
    private String mem_phone;
    @NotBlank(message = "{\"ko_KR\" : \"첨부파일 이미지를\"}")
    @NotNull(message = "{\"ko_KR\" : \"첨부파일 이미지를\"}")
    private String image_url;
    private String image_url2;
    private String image_url3;
    private String title;
    @NotBlank(message = "{\"ko_KR\" : \"내용을\"}")
    @NotNull(message = "{\"ko_KR\" : \"내용을\"}")
    private String contents;
    private Integer slct_device;
    private String device1;
    private String device2;
    private int del_yn;
    private int totalCnt = 0;
}

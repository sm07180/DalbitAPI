package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
public class KnowhowEventInputVo {

    private int page = 1;
    private int records = 50;
    private int slct_type = 0;
    private int slct_platform = 0;
    private int slct_order = 0;
    private int is_detail = 0;

    private int idx;
    private int event_idx;
    private int event_member_idx;
    private String mem_no;
    private int slct_device;

    private String device1;
    private String device2;
    private String title;
    private String contents;

    private String image_url;
    private String image_url2;
    private String image_url3;

    private int del_yn;
}

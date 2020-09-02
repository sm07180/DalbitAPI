package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KnowhowEventOutputVo {
    private int idx;
    private int event_idx;
    private String mem_no;
    private String mem_nick;
    private String image_url;
    private String image_url2;
    private String image_url3;
    private String title;
    private String contents;
    private int view_cnt;
    private int good_cnt;
    private int is_good;
    private String slct_device;
    private String device1;
    private String device2;
    private String reg_date;
    private int level;
    private String image_profile;
    private ImageVo profImg;
}

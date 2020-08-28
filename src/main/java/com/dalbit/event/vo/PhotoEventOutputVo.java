package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PhotoEventOutputVo {
    private int idx;
    private int event_idx;
    private String mem_no;
    private String mem_nick;
    private String image_url;
    private String contents;
    private String reg_date;
    private int level;
    private String image_profile;
    private ImageVo profImg;
}

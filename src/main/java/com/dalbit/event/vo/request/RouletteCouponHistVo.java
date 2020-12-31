package com.dalbit.event.vo.request;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RouletteCouponHistVo {
    private int type;
    private int status;
    private String mem_no;
    private String mem_nick;
    private String mem_sex;
    private String title;

    private transient String image_profile;
    private ImageVo profile_image_info;
    private String issue_date;
}

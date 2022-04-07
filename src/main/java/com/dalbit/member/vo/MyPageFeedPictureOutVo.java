package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class MyPageFeedPictureOutVo {
    private Long photo_no;
    private Long feed_reg_no;
    private Long mem_no;
    private String img_name;
    private Date ins_date;

    private ImageVo imgObj;
}

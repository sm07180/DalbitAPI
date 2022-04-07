package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
public class MyPageFeedReplyOutVo {
    private Long tail_no;
    private Long parent_no;
    private Long mem_no;
    private Long tail_mem_no;
    private String tail_conts;
    private String tail_mem_nick;
    private String tail_mem_sex;
    private String tail_image_profile;
    private String ins_date;
    private String upd_date;

    private ImageVo profImg;
}

package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter @Setter
public class MyPageFeedOutVo {
    private Long reg_no;
    private Long mem_no;
    private String mem_nick;
    private String mem_sex;
    private String image_profile;
    private String feed_conts;
    private String image_path;
    private Long tail_cnt;
    private Long rcv_like_cnt;
    private Long rcv_like_cancel_cnt;
    private String like_yn;
    private String ins_date;

    private List<MyPageFeedPictureOutVo> photoInfoList;
    private ImageVo profImg;
}

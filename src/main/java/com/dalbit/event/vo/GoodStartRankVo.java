package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodStartRankVo {
    private String good_no; // 이벤트 회차 번호
    private String mem_no; // 회원번호
    private String mem_id; // 아이디
    private String mem_sex; // 성별
    private String mem_nick; // 대화명
    private int mem_level; // 회원레벨
    private String image_profile; // 대표이미지
    private String tot_mem_score; // 랭킹점수

    private ImageVo profImg;
    private String holderBg;
    private String[] levelColor;
}

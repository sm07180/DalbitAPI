package com.dalbit.event.vo;

import com.dalbit.common.vo.ImageVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoodStartRankBjMySelVo {
    private String good_no; // 이벤트 회차 번호
    private String mem_no; // 회원번호
    private String mem_id; // 아이디
    private String mem_sex; // 성별
    private String mem_nick; // 대화명
    private int mem_level; // 회원레벨
    private String image_profile; // 대표이미지
    private String mem_dal_score; // 선물점수
    private String mem_tot_like_score; // 좋아요점수
    private String mem_booster_score; // 부스터점수
    private String tot_mem_score; // 랭킹점수
    private String my_rank_no; // 내순위

    private ImageVo profImg;
    private String holderBg;
    private String[] levelColor;

}

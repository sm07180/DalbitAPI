package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class GganbuMyBettingLogSelVo {
    private String gganbu_no; // 회차번호
    private String mem_no; // 회원번호 (신청자)
    private String mem_id; // 회원아이디 (신청자)
    private String mem_nick; // 회원대화명 (신청자)
    private String image_profile; // 회원사진(신청자)
    private int mem_level; // 회원레벨 (신청자)
    private int mem_state; // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...) (신청자)
    private String win_slct; // 승패여부[w:승, l:패]
    private int red_marble; // 배팅 빨간구슬
    private int yellow_marble; // 배팅 노란구슬
    private int blue_marble; // 배팅 파란구슬
    private int violet_marble; // 배팅 보라구슬
    private Date ins_date; // 등록일자
}

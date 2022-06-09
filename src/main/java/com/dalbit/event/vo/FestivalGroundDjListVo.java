package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FestivalGroundDjListVo {
    private int seq_no;              // 회차 번호
    private int rcv_byeol_cnt;       // 별
    private int new_fan_cnt;         // 신규팬
    private int play_cnt;            // 방송시간(초)
    private int play_score_cnt;      // 방송시간점수
    private int tot_score_cnt;       // 총점
    private String mem_no;           // 회원 번호
    private String mem_id;           // 회원 아이디
    private String mem_nick;         // 회원 닉네임
    private String mem_sex;          // 회원성별
    private String image_profile;    // 프로필
    private int mem_level;           // 레벨
    private String mem_state;        // 회원상태(1:정상3:블럭, 4:탈퇴, 5:영구정지...)
    private String ins_date;         // 등록일자
    private String upd_date;         // 수정일자
}

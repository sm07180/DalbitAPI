package com.dalbit.event.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GroundListVo {
    private String ground_no;       // 회차번호
    private String team_no;         // 팀번호
    private String team_name;       // 팀이름
    private String team_medal_code; // 팀 메달 코드(m000 형식)
    private String team_edge_code;  // 팀 테두리 코드(e000 형식)
    private String team_bg_code;    // 팀 배경 코드(b00 형식)
    private int rank_pt;         // 랭킹점수
    private int send_dal_cnt;    // 선물한달수
    private int rcv_byeol_cnt;   // 받은별수
    private int new_fan_cnt;     // 신규팬수
    private int play_time;       // 방송시간
    private int bonus_play_time; // 방송시간(가선점 시간 방송)
    private String ins_date;        // 등록일자
    private String upd_date;        // 수정일자

    private int time_rank_bonus; // 타임랭킹 가산점 (내 랭킹)
}

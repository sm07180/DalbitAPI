package com.dalbit.rank.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class TeamRankVO {

    private String the_week_date;   //DATE-- 집계일자
    private Integer team_no;        //BIGINT-- 팀번호
    private String team_name;       //VARCHAR(15)-- 팀이름
    private String team_conts;      //VARCHAR(200)-- 팀소개
    private String team_medal_code; //CHAR(4)-- 팀메달코드
    private String team_edge_code;  //CHAR(4)-- 팀 테두리코드
    private String team_bg_code;    //CHAR(4)-- 팀 배경코드
    private Integer rank_pt;        //INT-- 랭킹점수
    private Integer send_dal_cnt;   //BIGINT-- 보낸달수
    private Integer rcv_byeol_cnt;  //BIGINT-- 받은별수
    private Integer new_fan_cnt;    //BIGINT-- 신규팬수
    private Integer play_time;      //BIGINT-- 총방송시간
}

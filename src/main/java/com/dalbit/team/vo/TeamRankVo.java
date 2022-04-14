package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamRankVo {
  Integer the_week_date;	//DATE		-- 집계일자
  Integer team_no;			//BIGINT		-- 팀번호
  Integer team_name;		//VARCHAR(15)	-- 팀이름
  String team_conts;		//VARCHAR(200)	-- 팀소개
  String team_medal_code;	//CHAR(4)		-- 팀메달코드
  String team_edge_code;	//CHAR(4)		-- 팀 테두리코드
  String team_bg_code;		//CHAR(4)		-- 팀 배경코드
  String rank_pt;			//INT		-- 랭킹점수
  String send_dal_cnt;		//BIGINT		-- 보낸달수
  String rcv_byeol_cnt;		//BIGINT		-- 받은별수
  Integer new_fan_cnt;		//BIGINT		-- 신규팬수
  Integer play_time;		//BIGINT		-- 총방송시간
  Integer cnt;              //INT		-- 총건수
//


}

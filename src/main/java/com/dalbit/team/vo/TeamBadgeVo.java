package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamBadgeVo {

  Integer tot_bg_cnt;	    //INT		-- 총 활동배지수
  Integer  cnt;		    //INT		-- 획득배지수
  Integer  auto_no;			//BIGINT			-- 배지고유번호
  Integer team_no;			//BIGINT			-- 팀번호
  String bg_slct;			//CHAR(1)			-- 배지구분[a:활동배지, b:배경, e:테두리, m:메달]
  String bg_slct_name;		//VARCHAR(15)		-- 배지구분명
  String bg_code;			//CHAR(4)			-- 배지코드
  String bg_name;			//VARCHAR(15)		-- 배지이름
  String bg_conts;		    //VARCHAR(200)		-- 배지설명
  String bg_color_url;		//VARCHAR(500)		-- 배지URL(획득)
  String bg_black_url;		//VARCHAR(500)		-- 배지URL(미획득)
  Integer bg_bonus;		    //INT			-- 배지경험치
  Integer bg_objective;		//INT			-- 목표수치(활동배지용)
  Integer bg_achieve;		//INT			-- 달성수치(활동배지용)
  Integer bg_cnt;			//BIGINT			-- 사용중인팀수
  String use_yn;			//CHAR(1)			-- 사용여부
  String bg_achieve_yn;		//CHAR(1)			-- 획득여부
  String bg_represent_yn;   //CHAR(1)			-- 대표배지 설정여부
  String chrgr_name;		//VARCHAR(40)		-- 등록관리자
  String ins_date;		    //DATETIME		-- 등록일자
  String upd_date;		    //DATETIME		-- 수정일자




}

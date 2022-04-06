package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamResultVo {
   Integer team_no;			    //BIGINT		-- 팀번호
   Integer master_mem_no;		//BIGINT		-- 팀장회원번호
   String team_name;		        //VARCHAR(15)	-- 팀명
   String team_conts;		    //VARCHAR(200)	-- 팀소개
   String team_medal_code;		//CHAR(4)		-- 팀메달
   String team_edge_code;		//CHAR(4)		-- 팀테두리
   String team_bg_code;		    //CHAR(4)		-- 팀배경
   Integer team_mem_cnt;		    //TINYINT		-- 팀원수
   Integer team_req_mem_cnt;	    //TINYINT		-- 가입신청수
   Integer team_ivt_mem_cnt;	    //TINYINT		-- 가입초대수
   Integer team_score;		    //BIGINT		-- 팀점수
   Integer team_badge_score;	    //BIGINT		-- 팀 배지점수
   Integer team_bonus_score;	    //BIGINT		-- 팀 보너스점수
   Integer team_rank;		    //INT		-- 팀랭킹
   Integer team_chnge_cnt;		//TINYINT		-- 팀정보 수정횟수
   String req_mem_yn;		    //CHAR(1)		-- 가입신청 허용여부
   String ins_date;		        //DATETIME	-- 등록일자
   String upd_date;		        //DATETIME	-- 수정일자
   Integer tm_mem_no;		    //BIGINT		-- 팀원회원번호
   String tm_mem_nick;		    //VARCHAR(20)	-- 팀원대화명
   String tm_image_background;	//VARCHAR(256)	-- 프로필 배경사진
   Integer tm_mem_score;		    //BIGINT		-- 팀 기여점수

}

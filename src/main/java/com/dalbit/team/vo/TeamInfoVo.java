package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamInfoVo {
    Integer team_no;			    //BIGINT		-- 팀번호
    Long master_mem_no;		//BIGINT		-- 팀장회원번호
    String team_name;		        //VARCHAR(15)	-- 팀명
    String team_conts;		    //VARCHAR(200)	-- 팀소개
    String team_medal_code;		//CHAR(4)		-- 팀메달
    String team_edge_code;		//CHAR(4)		-- 팀테두리
    String team_bg_code;		    //CHAR(4)		-- 팀배경
    Integer team_mem_cnt;		    //TINYINT		-- 팀원수
    Integer team_req_mem_cnt;	    //TINYINT		-- 가입신청수
    Integer team_ivt_mem_cnt;	    //TINYINT		-- 가입초대수
    Integer team_tot_score;		    //BIGINT		-- 팀점수
    Integer rank_no;	    //BIGINT		-- 팀랭킹
    Integer rank_pt;	    //BIGINT		-- 팀랭킹 점수
    Integer team_chnge_cnt;		//TINYINT		-- 팀정보 수정횟수
    String req_mem_yn;		    //CHAR(1)		-- 가입신청 허용여부
    String ins_date;		        //DATETIME	-- 등록일자
    String upd_date;		        //DATETIME	-- 수정일자
}

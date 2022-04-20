package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamMemVo {
    Integer team_no;			    //BIGINT		-- 팀번호
    Long tm_mem_no;		            //BIGINT		-- 팀원회원번호
    String tm_mem_nick;		        //VARCHAR(20)	-- 팀원대화명
    String tm_image_profile;	    //VARCHAR(256)	-- 프로필 배경사진
    Integer tm_mem_score;		    //BIGINT		-- 팀 기여점수
    String team_mem_type;		    //CHAR(1)		-- 팀 멤버 구분[m:개설자 ,t:일반멤버]
    String ins_date;		        //DATETIME	-- 등록일자
    String upd_date;		        //DATETIME	-- 수정일자
}

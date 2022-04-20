package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamFanStarVo {
    Long mem_no;			    //BIGINT		-- 회원번호
    Long mem_no_star;		    //BIGINT		-- 회원번호 (나의스타)
    String mem_nick_star;		//VARCHAR(20)	-- 회원닉네임 (나의스타)
    String image_profile_star;  // 스타 프로필사진
    Integer mem_level_star;	    //SMALLINT	    -- 회원레벨 (나의스타)
    Long mem_no_fan;		    //BIGINT		-- 회원번호 (나의팬)
    String mem_nick_fan;		//VARCHAR(20)	-- 회원닉네임 (나의팬)
    Integer mem_level_fan;	    //SMALLINT	    -- 회원레벨 (나의팬)
    String image_profile_fan;  // 팬 프로필사진
    String team_yn;			    //CHAR(1)		-- 팀가입여부
    String team_req_yn;		    //CHAR(1)		-- 팀가입 초대 (미초대:n, 가입신청:r, 초대:i)
}

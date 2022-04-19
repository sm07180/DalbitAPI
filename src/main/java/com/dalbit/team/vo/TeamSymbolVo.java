package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TeamSymbolVo {
  private Integer auto_no;			    //BIGINT		-- 고유번호
  private String bg_slct;			    //CHAR(1)		-- 배지구분[b:배경, e:테두리, m:메달]
  private String bg_slct_name;		    //VARCHAR(15)	-- 배지구분명
  private String bg_code;			    //CHAR(4)		-- 팀 배지 코드(x000 형식)
  private String bg_name;			    //VARCHAR(15)	-- 배지 이름
  private String bg_conts;		        //VARCHAR(200)	-- 소개
  private String bg_url;			    //VARCHAR(500)	-- 배지 URL
  private Integer bg_cnt;			    //BIGINT		-- 사용중인팀수
  private String use_yn;			    //CHAR(1)		-- 사용여부
  private String chrgr_name;		    //VARCHAR(40)	-- 등록관리자명
  private String ins_date;		        //DATETIME	-- 등록일자
  private String upd_date;		        //DATETIME	-- 수정일자

  private Integer team_no;			    //BIGINT			-- 팀번호
  private String team_name;		        //VARCHAR(15)		-- 팀명
  private String team_medal_code;		//CHAR(4)			-- 팀메달
  private String team_edge_code;		//CHAR(4)			-- 팀테두리
  private String team_bg_code;		    //CHAR(4)			-- 팀배경
  private Integer req_cnt;			    //INT			-- 초대내역
  private String pageLink;              // 팀 - 클릭 했을 때 웹뷰 띄워줄 url
}

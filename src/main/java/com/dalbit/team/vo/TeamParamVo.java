package com.dalbit.team.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamParamVo {
    private Long memNo;                 //BIGINT		-- 개설자 회원번호
    private String teamName;            //VARCHAR(15)	-- 팀이름
    private String teamConts;           //VARCHAR(200)	-- 팀소개내용
    private String teamMedalCode;       //CHAR(4)		-- 팀 메달 코드(m000 형식)
    private String teamEdgeCode;        //CHAR(4)		-- 팀 테두리 코드(e000 형식)
    private String teamBgCode;          //CHAR(4)		-- 팀 배경 코드(b000 형식)
    private String updSlct;             //CHAR(1)		-- 수정구분[a:심볼및이름, b:소개수정] -- 뱃지[y:대표 설정, n:대표해제]
    private Integer teamNo;             //BIGINT		-- 팀번호
    private String reqMemYn;            //CHAR(1)		-- 가입신청 허용여부
    private Long masterMemNo;        //BIGINT		-- 팀장 회원번호
    private String chrgrName;           //VARCHAR(40)	-- 삭제 관리자명 (관리자 삭제시)
    private String reqSlct;             //CHAR(1)		-- 신청구분 [r:가입신청, i:초대]
    private String delSclt;             //CHAR(1)		-- 탈퇴구분 [ m:팀장탈퇴, t:본인탈퇴, c:관리자탈퇴, e:회원탈퇴 ]
    private Long tmMemNo;            //BIGINT		-- 팀원 회원번호
    private String bgCode;              //CHAR(4)		-- 배지코드
    private Integer pageNo=1;           //INT UNSIGNED		-- 페이지 번호
    private Integer pagePerCnt=100;     //INT UNSIGNED	-- 페이지 당 노출 건수 (Limit)
    private String symbolSlct;          //CHAR(1)		-- 심볼구분 [b:배경, e:테두리, m:메달]
    private String ordSlct;             //CHAR(1)		-- 정렬구분 [f:선호도, c:코드순, i:갱신일순]

    @JsonProperty("tDate")
    private String tDate;
}

package com.dalbit.team.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamParamVo {
    Integer memNo;              //BIGINT		-- 개설자 회원번호
    String teamName;            //VARCHAR(15)	-- 팀이름
    String teamConts;           //VARCHAR(200)	-- 팀소개내용
    String teamMedalCode;       //CHAR(4)		-- 팀 메달 코드(m000 형식)
    String teamEdgeCode;        //CHAR(4)		-- 팀 테두리 코드(e000 형식)
    String teamBgCode;          //CHAR(4)		-- 팀 배경 코드(b000 형식)
    String updSlct;             //CHAR(1)		-- 수정구분[a:심볼및이름, b:소개수정] -- 뱃지[y:대표 설정, n:대표해제]
    Integer teamNo;             //BIGINT		-- 팀번호
    String reqMemYn;            //CHAR(1)		-- 가입신청 허용여부
    Integer masterMemNo;        //BIGINT		-- 팀장 회원번호
    String chrgrName;           //VARCHAR(40)	-- 삭제 관리자명 (관리자 삭제시)
    String reqSlct;             //CHAR(1)		-- 신청구분 [r:가입신청, i:초대]
    String delSclt;             //CHAR(1)		-- 탈퇴구분 [ m:팀장탈퇴, t:본인탈퇴, c:관리자탈퇴, e:회원탈퇴 ]
    Integer tmMemNo;            //BIGINT		-- 팀원 회원번호
    String bgCode;              //CHAR(4)		-- 배지코드
}

package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoteResultVo {
    private String voteNo;                 //	bigint(20)	-- 투표번호
    private String memNo;                  //	bigint(20)	-- 투표개설자 회원번호
    private String roomNo;                 //	bigint(20)	-- 투표개설 방번호
    private String voteTitle;              //	varchar(30)	-- 투표제목
    private String voteEndSlct;            //	char(1)		-- 투표종료구분[s:투표중, e:마감, d:투표삭제]
    private String voteAnonyYn;            //	char(1)		-- 익명투표 여부
    private String voteDupliYn;            //	char(1)		-- 중복투표 여부
    private Integer voteMemCnt;            //	smallint(6)	-- 투표참여회원수
    private Integer voteItemCnt;           //	smallint(6)	-- 투표항목수
    private Integer endTime;               //	int(11)		-- 마감설정시간(초)
    private LocalDateTime startDate;       //	datetime(6)	-- 투표개설 일자
    private LocalDateTime endDate;         //	datetime(6)	-- 투표종료 일자
    private LocalDateTime insDate;         //	datetime(6)	-- 등록일자
    private LocalDateTime updDate;         //	datetime(6)	-- 변경일자
    private String itemNo;                 //	bigint(20)	-- 투표항목번호
    private String voteItemName;           //	varchar(30)	-- 투표 항목 이름
}

package com.dalbit.broadcast.vo;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VoteResultVo {
    private String voteNo;           // 투표번호
    private String memNo;            // 투표개설자 회원번호
    private String roomNo;           // 투표개설 방번호
    private String voteTitle;        // 투표제목
    private String voteEndSlct;      // 투표종료구분[s:투표중, e:마감, d:투표삭제]
    private String voteAnonyYn;      // 익명투표 여부
    private String voteDupliYn;      // 중복투표 여부
    private Integer voteMemCnt;      // 투표참여회원수
    private Integer voteItemCnt;     // 투표항목수
    private Integer endTime;         // 마감설정시간(초)
    private LocalDateTime startDate; // 투표개설 일자
    private LocalDateTime endDate;   // 투표종료 일자
    private LocalDateTime insDate;   // 등록일자
    private LocalDateTime updDate;   // 변경일자
    private String itemNo;           // 투표항목번호
    private String voteItemName;     // 투표 항목 이름
}

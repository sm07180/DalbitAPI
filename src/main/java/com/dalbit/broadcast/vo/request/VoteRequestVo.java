package com.dalbit.broadcast.vo.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VoteRequestVo {
    private String memNo;               // 회원번호(개설자)
    private String pmemNo;              // 회원번호(투표자)
    private String roomNo;              // 방번호
    private String voteNo;              // 투표번호
    private String itemNo;              // 투표 아이템 번호
    private String voteTitle;           // 투표제목
    private String voteSlct;            // 투표리스트 구분 [s:투표중, e:마감]
    private String voteAnonyYn;         // 중복투표 여부 [y:익명, n:일반]
    private String voteDupliYn;         // 중복투표 여부
    private Integer voteItemCnt;        // 투표항목수
    private Integer endTime;            // 마감설정시간 (초)
    private String endSlct;             // 마감구분 [a:전체마감, o:단일마감]

    private String voteItemName;        // 투표 아이템 제목
    private List<String> voteItemNames; // loop
}

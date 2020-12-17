package com.dalbit.clip.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ClipRecommendInfoVo {

    private String recDate;         // 날짜
    private String yearMonth;       // 년/월
    private int weekNo;             // 주차
    private String castNo;          // 클립번호
    private String castMemNo;       // 클립회원번호
    private int regCnt;             // 클립등록수
    private int enableFan;          // 팬여부
    private String titleMsg;        // 소개제목
    private String videoUrl;        // 비디오 url
    private String thumbUrl;        // 썸네일 url
    private String subjectType;     // 주제
    private String subjectName;     // 주제명
    private String title;           // 클립제목
    private String memNo;           // 요청회원번호
    private String memNick;         // 회원닉네임
    private String descMsg;         // 소개내용
    private int prevYn;         // 이전주 클립 여부
    private int nextYn;         // 다음주 클립 여부

}

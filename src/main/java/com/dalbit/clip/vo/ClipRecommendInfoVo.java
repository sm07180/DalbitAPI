package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipRecommendInfoVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ClipRecommendInfoVo {

    public ClipRecommendInfoVo() {}

    public ClipRecommendInfoVo(P_ClipRecommendInfoVo target) {
        setRecDate(target.getRecDate());
        setYearMonth(target.getYearMonth());
        setMonth(target.getYearMonth().substring(4,6));
        setWeekNo(target.getWeekNo());
        setClipNo(target.getCastNo());
        setClipMemNo(target.getCastMemNo());
        setRegCnt(target.getRegCnt());
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setTitleMsg(target.getTitleMsg());
        setVideoUrl(target.getVideoUrl());
        setThumbUrl(target.getThumbUrl());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setTitle(target.getTitle());
        setMemNo(target.getMemNo());
        setNickNm(target.getMemNick());
        setDescMsg(target.getDescMsg());
        setIsPrev(target.getPrevYn() == 1 ? true : false);
        setIsNext(target.getNextYn() == 1 ? true : false);
    }
    private String recDate;         // 날짜
    private String yearMonth;       // 년/월
    private String month;       // 월
    private int weekNo;             // 주차
    private String clipNo;          // 클립번호
    private String clipMemNo;       // 클립회원번호
    private int regCnt;             // 클립등록수
    private Boolean isFan;          // 팬여부
    private String titleMsg;        // 소개제목
    private String videoUrl;        // 비디오 url
    private String thumbUrl;        // 썸네일 url
    private String subjectType;     // 주제
    private String subjectName;     // 주제명
    private String title;           // 클립제목
    private String memNo;           // 요청회원번호
    private String nickNm;         // 회원닉네임
    private String descMsg;         // 소개내용
    private Boolean isPrev;         // 이전주 클립 여부
    private Boolean isNext;         // 다음주 클립 여부

}

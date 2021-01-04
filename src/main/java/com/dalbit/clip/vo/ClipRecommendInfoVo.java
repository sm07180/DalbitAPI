package com.dalbit.clip.vo;

import com.dalbit.clip.vo.procedure.P_ClipRecommendInfoVo;
import com.dalbit.common.vo.ImageVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;

@Getter @Setter
public class ClipRecommendInfoVo {

    public ClipRecommendInfoVo() {}

    public ClipRecommendInfoVo(P_ClipRecommendInfoVo target) {
        setRecDate(target.getRecDate().replace(" 00:00:00", ""));
        setClipNo(target.getCastNo());
        setClipMemNo(target.getCastMemNo());
        setRegCnt(target.getRegCnt());
        setIsFan(target.getEnableFan() == 0 ? true : false);
        setTitleMsg(target.getTitleMsg());
        setByeolCnt(target.getByeolCnt());
        setGoodCnt(target.getGoodCnt());
        setReplyCnt(target.getReplyCnt());
        setBannerUrl(target.getBannerUrl());
        setThumbUrl(target.getThumbUrl());
        setFbookUrl(target.getFbookUrl());
        setInstaUrl(target.getInstaUrl());
        setYtubeUrl(target.getYtubeUrl());
        setSubjectType(target.getSubjectType());
        setSubjectName(target.getSubjectName());
        setTitle(target.getTitle());
        setMemNo(target.getMemNo());
        setNickNm(target.getMemNick());
        setDescMsg(target.getDescMsg());

        Calendar calendar = Calendar.getInstance();
        String date = target.getRecDate();
        String[] dates = date.replace(" 00:00:00", "").split("-");
        int year = Integer.parseInt(dates[0]);
        int month = Integer.parseInt(dates[1]);
        int day = Integer.parseInt(dates[2]);
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(7);
        calendar.set(year, month - 1, day);

        setTime(date.substring(5,7).replace("-"," ")+"월 "+calendar.get(Calendar.WEEK_OF_MONTH)+"주차");
    }
    private String recDate;         // 날짜
    private String time;            // 화면에 보일 월, 주차
    private String clipNo;          // 클립번호
    private String clipMemNo;       // 클립회원번호
    private int regCnt;             // 클립등록수
    private Boolean isFan;          // 팬여부
    private String titleMsg;        // 소개제목
    private int byeolCnt;           // 받은 별 수
    private int goodCnt;            // 받은 좋아요 수
    private int replyCnt;           // 댓글 수
    private String bannerUrl;       // 배너 url
    private String thumbUrl;       // 썸네일 url
    private String fbookUrl;        // 페이스북 url
    private String instaUrl;        // 인스타 url
    private String ytubeUrl;        // 유튜브 url
    private String subjectType;     // 주제(코드)
    private String subjectName;     // 주제명
    private String title;           // 클립 제목
    private String memNo;           // 요청회원번호
    private String nickNm;          // 회원닉네임
    private String descMsg;         // 소개내용

}

package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ClipRecommendInfoVo extends P_ApiVo {

    private String recDate;         // 날짜
    private String castNo;          // 클립번호
    private String castMemNo;       // 클립회원번호
    private int regCnt;             // 클립등록수
    private int enableFan;          // 팬여부
    private String titleMsg;        // 소개제목
    private int byeolCnt;           // 받은 별 수
    private int goodCnt;            // 받은 좋아요 수
    private int replyCnt;           // 댓글 수
    private String bannerUrl;       // 배너 url
    private String thumbUrl;        // 썸네일 url
    private String fbookUrl;        // 페이스북 url
    private String instaUrl;        // 인스타 url
    private String ytubeUrl;        // 유튜브 url
    private String subjectType;     // 주제(코드)
    private String subjectName;     // 주제명
    private String title;           // 클립 제목
    private String memNo;           // 요청회원번호
    private String memNick;         // 회원닉네임
    private String descMsg;         // 소개내용

}

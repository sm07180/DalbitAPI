package com.dalbit.clip.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ClipRecommendListOutputVo extends P_ApiVo {

    public P_ClipRecommendListOutputVo() {
    }

    private String castNo;               //  클립번호
    private String backgroundImage;      // 클립이미지
    private String fileName;             // 파일이름
//    private String filePath;           // 파일경로
    private String filePlay;             // 재생시간
//    private String fileSize;           // 파일크기
    private String subjectType;          // 주제
    private String subjectName;          // 주제명
    private String title;                // 클립제목
    private String memNo;                // 회원번호
    private String memSex;               // 성별
    private String memNick;              // 닉네임
    private int leaderYn;                // 대표 여부 (0:일반 1:대표)
    private int byeolCnt;                // 받은 별 수
    private int goodCnt;                 //  받은 좋아요 수
    private int replyCnt;                //  받은 댓글 수

}

package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadBasicEditVo extends P_ApiVo {
    private String mem_no;
    private String subjectType;                                           //방송종류
    private String title;                               				  //제목
    private String backgroundImage;                                       //백그라운드 이미지 경로
    private int backgroundImageGrade;                                     //백그라운드 구글 선정성
    private String welcomMsg;                                             //환영 메시지
    private String notice;                                                //공지사항
    private int entryType;                                                //입장 (0:전체, 1:팬m 2: 20세이상)
}

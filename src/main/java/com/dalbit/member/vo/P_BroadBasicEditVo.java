package com.dalbit.member.vo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_BroadBasicEditVo {
    private String mem_no;
    private int subjectType;                                              //방송종류
    private String title;                               				  //제목
    private String backgroundImage;                                       //백그라운드 이미지 경로
    private int backgroundImageGrade;                                     //백그라운드 구글 선정성
    private String welcomMsg;                                             //환영 메시지
    private String notice;                                                //공지사항
    private int entry;                                                    //입장 (0:전체, 1:팬)
    private int age;                                                      //나이제한(0:전체, 1: 20세이상)

}

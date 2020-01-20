package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_BroadBasicEditVo {
    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private int subjectType = 1;                                                 //방송종류
    @Builder.Default private String title = "자유";                               				  //제목
    @Builder.Default private String backgroundImage = "/2020/01/10/15/asdas132213.jpg";           //백그라운드 이미지 경로
    @Builder.Default private int backgroundImageGrade = 3;                                        //백그라운드 구글 선정성
    @Builder.Default private String welcomMsg = "환영합니다";                                     //환영 메시지
    @Builder.Default private String notice = "공지사항";                                          //공지사항
    @Builder.Default private int entry = 0;                                                       //입장 (0:전체, 1:팬)
    @Builder.Default private int age = 0;                                                         //나이제한(0:전체, 1: 20세이상)

}

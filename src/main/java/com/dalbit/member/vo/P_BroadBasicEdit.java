package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_BroadBasicEdit {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private int subjectType = 1;
    @Builder.Default private String title = "자유";
    @Builder.Default private String backgroundImage = "/2020/01/10/15/asdas132213.jpg";
    @Builder.Default private int backgroundImageGrade = 3;
    @Builder.Default private String welcomMsg = "환영합니다";
    @Builder.Default private String notice = "공지사항";
    @Builder.Default private int entry = 0;
    @Builder.Default private int age = 0;

}

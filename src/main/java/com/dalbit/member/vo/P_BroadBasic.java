package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_BroadBasic {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private int subject_type = 2;
    @Builder.Default private String title = "테스트000002";
    @Builder.Default private String image_background = "http://backgroundImage.moonlight.co.kr/back00002.jpg";
    @Builder.Default private String msg_welcom = "환영합니다~00002.";
    @Builder.Default private int restrict_entry = 0;
    @Builder.Default private int restrict_age = 0;

}

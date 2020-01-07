package com.demo.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_ProfileEditVo {

    @Builder.Default private String mem_no = "11577931027280";
    @Builder.Default private String memSex = "m";
    @Builder.Default private String nickName = "미스터홍";
    @Builder.Default private String name = "홍길동";
    @Builder.Default private String birthYear = "1990";
    @Builder.Default private String birthMonth = "9";
    @Builder.Default private String birthDay = "11";
    @Builder.Default private String profileImage = "http://profileImage.profileImage";
    @Builder.Default private String backgroundImage = "http://backgroundImage.backgroundImage";
    @Builder.Default private String profileMsg = "안녕하세요~ test 입니다.";


}

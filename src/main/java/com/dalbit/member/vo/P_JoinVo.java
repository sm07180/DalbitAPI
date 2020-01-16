package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class P_JoinVo {

    @Builder.Default private String memSlct = "p";
    @Builder.Default private String id = "010-1234-4568";
    @Builder.Default private String pw = "1234";
    @Builder.Default private String memSex = "m";
    @Builder.Default private String nickName = "radiagaga";
    @Builder.Default private int birthYear = 1980;
    @Builder.Default private int birthMonth = 11;
    @Builder.Default private int birthDay = 23;
    @Builder.Default private String terms1 = "y";
    @Builder.Default private String terms2 = "y";
    @Builder.Default private String terms3 = "y";
    @Builder.Default private String profileImage = "http://profileImage.profileImage";
    @Builder.Default private String name = "홍길동";
    @Builder.Default private String email = "test@inforex.co.kr";
    @Builder.Default private int os = 1;
    @Builder.Default private String deviceUuid = "2200DDD1-77A9-493E-8AF8-0C98FD820AB1";
    @Builder.Default private String deviceToken = "45E3156FDE20E7F11AF59BEFAF26CEC566D5ADBA4AABCA22854CB77381456F81";
    @Builder.Default private String appVersion = "1.0.0.1";
}

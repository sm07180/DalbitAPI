package com.demo.member.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginVo{

    @Builder.Default private String memSlct = "p";
    @Builder.Default private String id = "010-1234-45683";
    @Builder.Default private String pw = "1234";
    @Builder.Default private String os = "1";
    @Builder.Default private String deviceUuid = "123";
    @Builder.Default private String deviceToken = "1234";
    @Builder.Default private String appVersion = "1.0.0.1";
}

package com.demo.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class LoginVo{

    @Builder.Default private String memSlct = "p";
    @Builder.Default private String id = "010-1234-4568";
    @Builder.Default private String pw = "1234";
    @Builder.Default private String os = "1";
    @Builder.Default private String deviceUuid = "2200DDD1-77A9-493E-8AF8-0C98FD820AB1";
    @Builder.Default private String deviceToken = "45E3156FDE20E7F11AF59BEFAF26CEC566D5ADBA4AABCA22854CB77381456F81";
    @Builder.Default private String appVersion = "1.0.0.1";
}

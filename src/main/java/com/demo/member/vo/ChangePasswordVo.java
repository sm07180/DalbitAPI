package com.demo.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ChangePasswordVo {

    @Builder.Default private String _phoneNo = "010-1234-4568";
    @Builder.Default private String _password = "1234";
}

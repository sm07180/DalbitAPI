package com.dalbit.member.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_ChangePasswordVo {

    private String _phoneNo;    // 핸드폰 번호
    private String _password;   // 변경할 비밀 번호
}

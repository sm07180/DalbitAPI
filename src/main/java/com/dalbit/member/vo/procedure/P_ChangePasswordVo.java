package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_ChangePasswordVo extends P_ApiVo {

    private String phoneNo;    // 핸드폰 번호
    private String password;   // 변경할 비밀 번호

}


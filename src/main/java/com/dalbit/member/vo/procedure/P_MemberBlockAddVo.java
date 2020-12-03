package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberBlockAddVo extends P_ApiVo {

    private String mem_no;
    private String blocked_mem_no;                      //차단회원번호
}

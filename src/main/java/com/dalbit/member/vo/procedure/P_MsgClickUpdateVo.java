package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MsgClickUpdateVo extends P_ApiVo {

    private String mem_no;
    private int msg_slct;
    private int msg_idx;
}

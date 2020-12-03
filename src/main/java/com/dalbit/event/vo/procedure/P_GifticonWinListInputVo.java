package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_GifticonWinListInputVo extends P_ApiVo {

    private String mem_no;
    private int slct_type;
}

package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.RoulettePhoneVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_RoulettePhoneVo extends P_ApiVo {

    public P_RoulettePhoneVo(){}
    public P_RoulettePhoneVo(RoulettePhoneVo roulettePhoneVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setPhone(roulettePhoneVo.getPhone());
        setWinIdx(roulettePhoneVo.getWinIdx());
    }

    private String mem_no;
    private String phone;
    private int winIdx;
}

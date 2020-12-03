package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FanEditVo extends P_ApiVo {

    public P_FanEditVo(){}
    public P_FanEditVo(FanEditVo fanEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setDelete_fan_list(fanEditVo.getFanNoList());
    }


    /* Input */
    private String mem_no;
    private String delete_fan_list;

}

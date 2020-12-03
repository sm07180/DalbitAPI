package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanListVo;
import com.dalbit.member.vo.request.FanMemoVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FanMemoVo extends P_ApiVo {

    public P_FanMemoVo(){}
    public P_FanMemoVo(FanMemoVo fanMemoVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setFan_mem_no(fanMemoVo.getMemNo());
    }


    /* Input */
    private String mem_no;
    private String fan_mem_no;

    /* Output */
    private String fanMemo;
}

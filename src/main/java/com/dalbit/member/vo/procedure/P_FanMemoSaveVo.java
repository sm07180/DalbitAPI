package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanMemoSaveVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_FanMemoSaveVo {

    public P_FanMemoSaveVo(){}
    public P_FanMemoSaveVo(FanMemoSaveVo fanMemoSaveVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setFan_mem_no(fanMemoSaveVo.getMemNo());
        setMemo(fanMemoSaveVo.getMemo());
    }


    /* Input */
    private String mem_no;
    private String fan_mem_no;
    private String memo;
}

package com.dalbit.broadcast.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberBroadcastingCheckVo extends P_ApiVo {

    public P_MemberBroadcastingCheckVo(){}
    public P_MemberBroadcastingCheckVo(String mem_no){
        setMem_no(mem_no);
    }

    private String mem_no;
}

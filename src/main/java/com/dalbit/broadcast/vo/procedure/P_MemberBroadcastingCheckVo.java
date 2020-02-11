package com.dalbit.broadcast.vo.procedure;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class P_MemberBroadcastingCheckVo {

    public P_MemberBroadcastingCheckVo(){}
    public P_MemberBroadcastingCheckVo(String mem_no){
        setMem_no(mem_no);
    }

    private String mem_no;
}

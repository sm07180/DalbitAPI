package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MoonVo;
import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MoonCheckVo extends P_ApiVo {
    public P_MoonCheckVo(){}
    public P_MoonCheckVo(MoonVo moonVo){
        setRoom_no(moonVo.getRoomNo());
    }
    private String room_no;
}

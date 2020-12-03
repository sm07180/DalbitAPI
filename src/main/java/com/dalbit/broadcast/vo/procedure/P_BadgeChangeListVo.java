package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.BadgeChangeVo;
import com.dalbit.common.vo.P_ApiVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class P_BadgeChangeListVo extends P_ApiVo {

    public P_BadgeChangeListVo(){}
    public P_BadgeChangeListVo(BadgeChangeVo badgeChangeVo){
        setSlctType(badgeChangeVo.getSlctType());
    }

    /* Input */
    private int slctType;

    /* Output */
    private String room_no;
    private String mem_no;

}

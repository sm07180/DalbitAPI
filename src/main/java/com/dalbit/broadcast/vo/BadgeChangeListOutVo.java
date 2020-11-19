package com.dalbit.broadcast.vo;

import com.dalbit.broadcast.vo.procedure.P_BadgeChangeListVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
public class BadgeChangeListOutVo {

    public BadgeChangeListOutVo(P_BadgeChangeListVo target){
        setRoomNo(target.getRoom_no());
        setMemNo(target.getMem_no());
    }
    private String roomNo;
    private String memNo;
}

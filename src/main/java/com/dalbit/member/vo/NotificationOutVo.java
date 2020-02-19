package com.dalbit.member.vo;

import com.dalbit.member.vo.procedure.P_NotificationVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationOutVo {

    private int notiType;
    private String contents;
    private String memNo;
    private String roomNo;
    private String regDt;
    private long regTs;

    public NotificationOutVo(P_NotificationVo target) {
        setNotiType(target.getNotiType());
        setContents(target.getContents());
        setMemNo(target.getTarget_mem_no());
        setRoomNo(target.getRoom_no());
        setRegDt(DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.getUTCTimeStamp(target.getRegDate()));
    }


}

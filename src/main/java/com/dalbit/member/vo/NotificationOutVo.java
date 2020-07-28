package com.dalbit.member.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.member.vo.procedure.P_NotificationVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NotificationOutVo {

    private int notiIdx;
    private int notiType;
    private String contents;
    private String memNo;
    private String roomNo;
    private String link;
    private String regDt;
    private long regTs;
    private ImageVo profImg;

    public NotificationOutVo(P_NotificationVo target) {
        setNotiIdx(target.getNotiIdx());
        setNotiType(target.getNotiType());
        setContents(target.getContents());
        setMemNo(target.getTarget_mem_no());
        setRoomNo(target.getRoom_no());
        setLink(target.getLink());
        setRegDt(DalbitUtil.getUTCFormat(target.getRegDate()));
        setRegTs(DalbitUtil.getUTCTimeStamp(target.getRegDate()));
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
    }


}

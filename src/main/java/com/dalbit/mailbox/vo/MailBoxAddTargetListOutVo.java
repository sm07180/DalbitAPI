package com.dalbit.mailbox.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxAddTargetListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailBoxAddTargetListOutVo {

    private String memNo;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int giftedByeol;
    private int listenTime;
    private String lastListenDt;
    private long lastListenTs;

    public MailBoxAddTargetListOutVo(P_MailBoxAddTargetListVo target){
        setMemNo(target.getTarget_mem_no());
        setNickNm(target.getNickName());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setGiftedByeol(target.getGiftedByeol());
        setListenTime(target.getListenTime());
        setLastListenDt(DalbitUtil.isEmpty(target.getLastlistenDate()) ? "" : DalbitUtil.getUTCFormat(target.getLastlistenDate()));
        setLastListenTs(DalbitUtil.isEmpty(target.getLastlistenDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getLastlistenDate()));

    }
}

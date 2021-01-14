package com.dalbit.mailbox.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxImageListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MailBoxImageListOutVo {

    private String msgIdx;
    private String memNo;
    private String memNickNm;
    private ImageVo imageInfo;
    private Boolean isDelete;
    private String sendDt;
    private long sendTs;

    public MailBoxImageListOutVo(P_MailBoxImageListVo target){
        setMsgIdx(target.getMsgIdx());
        setMemNo(target.getMemNo());
        setMemNickNm(target.getMemNick());
        setImageInfo(new ImageVo(target.getImageUrl(), DalbitUtil.getProperty("server.photo.url")));
        setIsDelete(target.getIsDelete() == 1 ? true : false);
        setSendDt(DalbitUtil.getUTCFormat(target.getMsgDate()));
        setSendTs(DalbitUtil.getUTCTimeStamp(target.getMsgDate()));
    }
}

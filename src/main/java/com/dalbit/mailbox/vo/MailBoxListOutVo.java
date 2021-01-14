package com.dalbit.mailbox.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxAddTargetListVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter @Setter
public class MailBoxListOutVo {

    private String memNo;
    private String chatNo;
    //private String title;
    private String nickNm;
    private String gender;
    private ImageVo profImg;
    private int unReadCnt;
    private int msgType;
    private String msg;
    private String sendDt;
    private long sendTs;
    private ImageVo imageInfo;
    private ItemInfoVo itemInfo;

    public MailBoxListOutVo(P_MailBoxListVo target){
        setMemNo(target.getTarget_mem_no());
        setChatNo(target.getChat_no());
        //setTitle(DalbitUtil.isEmpty(target.getTitle()) ? "알수없는사용자" : target.getTitle());
        setNickNm(DalbitUtil.isEmpty(target.getTitle()) ? "알수없는사용자" : target.getTitle());
        setGender(target.getMemSex());
        setProfImg(new ImageVo(target.getProfileImage(), target.getMemSex(), DalbitUtil.getProperty("server.photo.url")));
        setUnReadCnt(target.getUnreadCnt());
        setMsgType(target.getMsgType());    //대화구분(1: 글자, 2: 이미지, 3: 아이템선물)
        if(target.getMsgType() == 2){
            setMsg("사진을 보냈습니다.");
        }else if(target.getMsgType() == 3){
            setMsg("선물을 보냈습니다.");
        }else{
            setMsg(DalbitUtil.isEmpty(target.getLastMsg()) ? "" : target.getLastMsg());
        }
        setImageInfo((!DalbitUtil.isEmpty(target.getAddData2()) && "y".equals(target.getAddData2().toLowerCase())) ? new ImageVo() : new ImageVo(target.getAddData1(), DalbitUtil.getProperty("server.photo.url")));

        if(target.getMsgType() == 3){
            setItemInfo(new ItemInfoVo(target));
        }

        setSendDt(DalbitUtil.isEmpty(target.getLastMsgDate()) ? "" : DalbitUtil.getUTCFormat(target.getLastMsgDate()));
        setSendTs(DalbitUtil.isEmpty(target.getLastMsgDate()) ? 0 : DalbitUtil.getUTCTimeStamp(target.getLastMsgDate()));
    }
}

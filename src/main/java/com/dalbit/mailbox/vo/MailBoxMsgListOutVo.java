package com.dalbit.mailbox.vo;

import com.dalbit.common.vo.ImageVo;
import com.dalbit.mailbox.vo.procedure.P_MailBoxMsgListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

@Getter @Setter
public class MailBoxMsgListOutVo {

    private String msgIdx;
    private String memNo;
    private String nickNm;
    private int chatType;
    private String msg;
    private String addData1;
    private String addData2;
    private String addData3;
    private String addData4;
    private Boolean isRead;
    private String sendDt;
    private long sendTs;
    private ImageVo imageInfo;
    private ItemInfoVo itemInfo;
    private ImageVo profImg;
    private String targetMemNo;

    public MailBoxMsgListOutVo(P_MailBoxMsgListVo target, HashMap targetMap){
        setMsgIdx(target.getMsgIdx());
        setMemNo(target.getMem_no());
        setNickNm(target.getNickName());
        setChatType(target.getChatType());
        if(target.getChatType() == 2){
            if("y".equals(target.getAddData2().toLowerCase())){
                setMsg("삭제된 이미지입니다.");
            }else{
                setMsg("이미지입니다.");
            }
        }else if(target.getChatType() == 3){
            setMsg("선물을 보냈습니다.");
        }else{
            setMsg(DalbitUtil.isEmpty(target.getMsg()) ? "" : target.getMsg());
        }
        setImageInfo("y".equals(target.getAddData2().toLowerCase()) ? new ImageVo() : new ImageVo(target.getAddData1(), DalbitUtil.getProperty("server.photo.url")));
        setItemInfo(target.getChatType() == 3 ? new ItemInfoVo(target) : new ItemInfoVo());

        setIsRead(target.getReadYn() == 1 ? true : false);
        setSendDt(DalbitUtil.getUTCFormat(target.getSendDate()));
        setSendTs(DalbitUtil.getUTCTimeStamp(target.getSendDate()));
        setProfImg(new ImageVo(DalbitUtil.getStringMap(targetMap, "profileImage"), DalbitUtil.getStringMap(targetMap, "memSex"), DalbitUtil.getProperty("server.photo.url")));
        setTargetMemNo(DalbitUtil.getStringMap(targetMap, "target_mem_no"));
    }

}

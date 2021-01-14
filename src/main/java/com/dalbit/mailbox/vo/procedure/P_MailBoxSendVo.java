package com.dalbit.mailbox.vo.procedure;

import com.dalbit.common.vo.DeviceVo;
import com.dalbit.mailbox.vo.request.MailBoxSendVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MailBoxSendVo {

    public P_MailBoxSendVo(){}
    public P_MailBoxSendVo(MailBoxSendVo mailBoxSendVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxSendVo.getChatNo());
        setChatType(mailBoxSendVo.getChatType());
        setMsg(mailBoxSendVo.getMsg());
        setMsg_no(mailBoxSendVo.getMsgNo());
        setAddData1(mailBoxSendVo.getAddData1());
        setAddData2(mailBoxSendVo.getAddData2());
        setAddData3(mailBoxSendVo.getAddData3());
        setOs(new DeviceVo(request).getOs());
    }

    private String mem_no;
    private String chat_no;
    private int chatType;
    private String msg;
    private String msg_no;
    private String addData1;    //부가정보1 (image: URL, gift: itemCode)
    private String addData2;    //부가정보2 (gift: itemCnt)
    private String addData3;    //부가정보3 (gift: itemType)
    private int os;
}

package com.dalbit.mailbox.vo.procedure;

import com.dalbit.mailbox.vo.request.MailBoxImageDeleteVo;
import com.dalbit.mailbox.vo.request.MailBoxImageListVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter @ToString
public class P_MailBoxImageDeleteVo {

    public P_MailBoxImageDeleteVo(){}
    public P_MailBoxImageDeleteVo(MailBoxImageDeleteVo mailBoxImageDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxImageDeleteVo.getChatNo());
        setMsgIdx(mailBoxImageDeleteVo.getMsgIdx());
    }

    /* Input */
    private String mem_no;
    private String chat_no;
    private String msgIdx;

}

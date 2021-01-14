package com.dalbit.mailbox.vo.procedure;

import com.dalbit.mailbox.vo.request.MailBoxExitVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MailBoxExitVo {
    public P_MailBoxExitVo(){}
    public P_MailBoxExitVo(MailBoxExitVo mailBoxExitVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxExitVo.getChatNo());
    }

    private String mem_no;
    private String chat_no;
}

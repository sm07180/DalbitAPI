package com.dalbit.mailbox.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.mailbox.vo.request.MailBoxIsUseVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MailBoxIsUseVo extends P_ApiVo {

    public P_MailBoxIsUseVo(){}
    public P_MailBoxIsUseVo(MailBoxIsUseVo mailBoxIsUseVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setMailboxOnOff("true".equals(mailBoxIsUseVo.getIsMailboxOn()) || "1".equals(mailBoxIsUseVo.getIsMailboxOn()) ? 1 : 0);
    }

    private String mem_no;
    private int mailboxOnOff;
}

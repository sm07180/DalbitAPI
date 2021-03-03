package com.dalbit.mailbox.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.mailbox.vo.request.MailBoxReadVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MailBoxReadVo extends P_ApiVo {
    public P_MailBoxReadVo(){}
    public P_MailBoxReadVo(MailBoxReadVo mailBoxReadVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxReadVo.getChatNo());
    }

    private String mem_no;
    private String chat_no;
}

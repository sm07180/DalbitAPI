package com.dalbit.mailbox.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.mailbox.vo.request.MailBoxEnterVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MailBoxEnterVo extends P_ApiVo {
    public P_MailBoxEnterVo(){}
    public P_MailBoxEnterVo(MailBoxEnterVo mailBoxEnterVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(mailBoxEnterVo.getMemNo());
    }

    private String mem_no;
    private String target_mem_no;
}

package com.dalbit.mailbox.vo.procedure;

import com.dalbit.mailbox.vo.request.MailBoxImageListVo;
import com.dalbit.mailbox.vo.request.MailBoxMsgListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter @ToString
public class P_MailBoxImageListVo {

    public P_MailBoxImageListVo(){}
    public P_MailBoxImageListVo(MailBoxImageListVo mailBoxImageListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxImageListVo.getChatNo());
        setMsgIdx(mailBoxImageListVo.getMsgIdx());
    }

    /* Input */
    private String mem_no;
    private String chat_no;
    private String msgIdx;

    /* Output */
    private String memNo;
    private String memNick;
    private String imageUrl;
    private int isDelete;
    private Date msgDate;

}

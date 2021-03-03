package com.dalbit.mailbox.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.mailbox.vo.request.MailBoxMsgListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter @ToString
public class P_MailBoxMsgListVo extends P_ApiVo {

    public P_MailBoxMsgListVo(){}
    public P_MailBoxMsgListVo(MailBoxMsgListVo mailBoxMsgListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mailBoxMsgListVo.getPage()) ? 1 : mailBoxMsgListVo.getPage();
        //int pageCnt = DalbitUtil.isEmpty(mailBoxMsgListVo.getRecords()) ? 10 : mailBoxMsgListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setChat_no(mailBoxMsgListVo.getChatNo());
        setNowIdx(mailBoxMsgListVo.getNowIdx());
        setPageNo(pageNo);
        setPageCnt(mailBoxMsgListVo.getRecords());
    }

    /* Input */
    private String mem_no;
    private String chat_no;
    private String nowIdx;
    private int pageNo;         //현재 페이지 번호
    private int pageCnt;        //페이지당 리스트 개수

    /* Output */
    private String msgIdx;
    private String memNo;
    private String nickName;
    private int chatType;
    private String msg;
    private String addData1;
    private String addData2;
    private String addData3;
    private String addData4;
    private String addData5;
    private String addData6;
    private String addData7;
    private String addData8;
    private String addData9;
    private int readYn;
    private Date sendDate;
    private int isExpire;

}

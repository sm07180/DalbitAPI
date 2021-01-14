package com.dalbit.mailbox.vo.procedure;

import com.dalbit.mailbox.vo.request.MailBoxAddTargetListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter @ToString
public class P_MailBoxAddTargetListVo {

    public P_MailBoxAddTargetListVo(){}
    public P_MailBoxAddTargetListVo(MailBoxAddTargetListVo mailBoxAddTargetListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(mailBoxAddTargetListVo.getPage()) ? 1 : mailBoxAddTargetListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(mailBoxAddTargetListVo.getRecords()) ? 10 : mailBoxAddTargetListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(mailBoxAddTargetListVo.getSlctType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* Input */
    private String mem_no;
    private int slctType;
    private int pageNo;         //현재 페이지 번호
    private int pageCnt;        //페이지당 리스트 개수

    /* Output */
    private String target_mem_no;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int giftedByeol;
    private int listenTime;
    private Date lastlistenDate;
}

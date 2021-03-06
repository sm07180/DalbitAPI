package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.NotificationVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_NotificationVo extends P_ApiVo {

    public P_NotificationVo(){}
    public P_NotificationVo(NotificationVo notificationVo, HttpServletRequest request){

        int pageNo = DalbitUtil.isEmpty(notificationVo.getPage()) ? 1 : notificationVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(notificationVo.getRecords()) ? 10 : notificationVo.getRecords();
        setMem_no(MemberVo.getMyMemNo(request));
        setPageNo(pageNo);
        setPageCnt(pageCnt);

    }
    /* Input */
    private String mem_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int notiIdx;            //알림번호
    private int notiType;           // 알림종류
    private String contents;        // 알림내용
    private String target_mem_no;   // 알림 주체 회원번호
    private String room_no;         // DJ 방송방 번호
    private String link;         // 이동 URL
    private Date regDate;           // 작성일자
    private String memSex;
    private String profileImage;

}

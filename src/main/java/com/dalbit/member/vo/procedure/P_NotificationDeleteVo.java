package com.dalbit.member.vo.procedure;


import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.NotificationDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_NotificationDeleteVo extends P_ApiVo {

    public P_NotificationDeleteVo(){}
    public P_NotificationDeleteVo(NotificationDeleteVo notificationDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setDelete_notiIdx(notificationDeleteVo.getDelete_notiIdx());
    }
    /* input */
    private String mem_no;          // 요청 회원번호
    private String delete_notiIdx;  // 삭제할 알림 idx
}

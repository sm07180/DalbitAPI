package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.NotificationVo;
import com.dalbit.member.vo.request.ReadNotificationVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_ReadNotificationVo extends P_ApiVo {

    public P_ReadNotificationVo(){}
    public P_ReadNotificationVo(ReadNotificationVo readNotificationVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setNotiIdx(readNotificationVo.getNotiIdx());
    }

    /* Input */
    private String mem_no;
    private Integer notiIdx;

}

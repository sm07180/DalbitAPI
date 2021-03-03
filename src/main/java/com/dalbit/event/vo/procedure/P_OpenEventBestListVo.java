package com.dalbit.event.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.event.vo.request.OpenEventVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter @ToString
public class P_OpenEventBestListVo extends P_ApiVo {
    public P_OpenEventBestListVo(){}
    public P_OpenEventBestListVo(OpenEventVo openEventVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(openEventVo.getSlctType());
        setEvent_no(openEventVo.getEventNo());
    }

    /* Input */
    private String mem_no;
    private int slct_type;
    private int event_no;

    /* Oupput */
    private Date the_date;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int specialdjBadge;
    private int level;
    private String grade;




}

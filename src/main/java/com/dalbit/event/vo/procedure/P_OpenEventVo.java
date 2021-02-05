package com.dalbit.event.vo.procedure;

import com.dalbit.event.vo.request.OpenEventVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_OpenEventVo {
    public P_OpenEventVo(){}
    public P_OpenEventVo(OpenEventVo openEventVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlct_type(openEventVo.getSlctType());
        setEvent_no(openEventVo.getEventNo());
    }

    /* Input */
    private String mem_no;
    private int slct_type;
    private int event_no;

    /* Output */
    private int rank;
    private int level;
    private String grade;
    private String nickName;
    private String memSex;
    private String profileImage;
    private int totalPoint;
}

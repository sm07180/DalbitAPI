package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_MemberReceiveListVo {
    public P_MemberReceiveListVo(){}
    public P_MemberReceiveListVo(HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
    }
    /* Input */
    private String mem_no;
    /* Output */
    private int idx;
    private String mem_no_star;
    private String mem_sex;
    private String profileImage;
    private String mem_nick_star;
    private Date last_upd_date;

}

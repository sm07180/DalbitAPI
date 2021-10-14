package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameWinListVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MiniGameWinListVo {
    public P_MiniGameWinListVo(){}
    public P_MiniGameWinListVo(MiniGameWinListVo miniGameWinListVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo());
        setRoom_no(miniGameWinListVo.getRoomNo());
    }

    private String room_no;
    private String mem_no;
    private String mem_nick;
    private String mem_sex;
    private String mem_birth_year;
    private Integer pay_amt;
    private String opt;
    private String last_upd_date;
    private Object image_profile;
}

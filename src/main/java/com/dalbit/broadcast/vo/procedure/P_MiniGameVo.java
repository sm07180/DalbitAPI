package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MiniGameVo extends P_ApiVo {

    public P_MiniGameVo(){}
    public P_MiniGameVo(MiniGameVo miniGameVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(miniGameVo.getRoomNo());
        setGame_no(miniGameVo.getGameNo());
    }

    private String mem_no;
    private String room_no;
    private int game_no;
}

package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameEndVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MiniGameEndVo extends P_ApiVo {
    public P_MiniGameEndVo(){}
    public P_MiniGameEndVo(MiniGameEndVo miniGameEndVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(miniGameEndVo.getRoomNo());
        setGame_no(miniGameEndVo.getGameNo());
    }

    private String mem_no;
    private String room_no;
    private int game_no;
}

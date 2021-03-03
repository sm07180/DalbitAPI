package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameEditVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MiniGameEditVo extends P_ApiVo {

    public P_MiniGameEditVo(){}
    public P_MiniGameEditVo(MiniGameEditVo miniGameEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(miniGameEditVo.getRoomNo());
        setGame_no(miniGameEditVo.getGameNo());
        setPayYn("true".equals(miniGameEditVo.getIsFree().toLowerCase()) || "1".equals(miniGameEditVo.getIsFree()) ? 0 : 1);
        setPayAmt(miniGameEditVo.getPayAmt());
        setOptCnt(miniGameEditVo.getOptCnt());
        setOptList(miniGameEditVo.getOptList());
    }

    private String mem_no;
    private String room_no;
    private int game_no;
    private int payYn;
    private int payAmt;
    private int optCnt;
    private String optList;
}

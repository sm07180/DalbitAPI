package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameAddVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MiniGameAddVo extends P_ApiVo {

    public P_MiniGameAddVo(){}
    public P_MiniGameAddVo(MiniGameAddVo miniGameAddVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(miniGameAddVo.getRoomNo());
        setGame_no(miniGameAddVo.getGameNo());
        setPayYn("true".equals(miniGameAddVo.getIsFree().toLowerCase()) || "1".equals(miniGameAddVo.getIsFree()) ? 0 : 1);
        setPayAmt(miniGameAddVo.getPayAmt());
        setOptCnt(miniGameAddVo.getOptCnt());
        setOptList(miniGameAddVo.getOptList());
        setAutoYn(miniGameAddVo.getAutoYn());
    }

    private String mem_no;
    private String room_no;
    private int game_no;
    private int payYn;
    private int payAmt;
    private int optCnt;
    private String optList;
    private String autoYn;
}

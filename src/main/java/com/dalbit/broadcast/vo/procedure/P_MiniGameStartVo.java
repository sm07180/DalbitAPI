package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameStartVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter @ToString
public class P_MiniGameStartVo extends P_ApiVo {
    public P_MiniGameStartVo(){}
    public P_MiniGameStartVo(MiniGameStartVo miniGameStartVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setRoom_no(miniGameStartVo.getRoomNo());
        setGame_no(miniGameStartVo.getGameNo());
        setRoulette_no(miniGameStartVo.getRouletteNo());
        setVersionIdx(miniGameStartVo.getVersionIdx());
    }

    private String mem_no;
    private String room_no;
    private int game_no;
    private int roulette_no;
    private int versionIdx;

    private String autoYn;
}

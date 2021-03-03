package com.dalbit.broadcast.vo.procedure;

import com.dalbit.broadcast.vo.request.MiniGameListVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MiniGameListVo extends P_ApiVo {
    public P_MiniGameListVo(){}
    public P_MiniGameListVo(MiniGameListVo miniGameListVo, HttpServletRequest request){
        setRoom_no(miniGameListVo.getRoomNo());
        setMem_no(MemberVo.getMyMemNo(request));
    }

    /* Input */
    private String room_no;
    private String mem_no;

    /* Output */
    private String gameName;
    private String imageUrl;
    private String gameDesc;
    private int gameNo;
}



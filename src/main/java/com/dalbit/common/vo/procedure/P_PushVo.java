package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.common.vo.request.PushVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_PushVo extends P_ApiVo {

    public P_PushVo(){}
    public P_PushVo(PushVo pushVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctPush(pushVo.getPushType());
        setTitle(pushVo.getTitle());
        setContents(pushVo.getContents());
        setImageUrl(pushVo.getImageUrl());
        setPush_type(pushVo.getPushMoveType());
        setRoom_no(pushVo.getRoomNo());
        setTarget_mem_no(pushVo.getTarget_mem_no());
        setBoard_idx(pushVo.getBoardIdx());
    }

    private String mem_no;
    private Integer slctPush;
    private String title;
    private String contents;
    private String imageUrl;
    private String push_type;
    private String room_no;
    private String target_mem_no;
    private String board_idx;
}

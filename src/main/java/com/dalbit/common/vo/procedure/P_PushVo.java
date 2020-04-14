package com.dalbit.common.vo.procedure;

import com.dalbit.common.vo.request.PushVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_PushVo {

    public P_PushVo(){}
    public P_PushVo(PushVo pushVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctPush(pushVo.getPushType());
        setTitle(pushVo.getTitle());
        setContents(pushVo.getContents());
        setImageUrl(pushVo.getImageUrl());
        setPush_type(pushVo.getPushMoveType());
        setRoom_no(pushVo.getRoomNo());
        setTartget_mem_no(pushVo.getTartget_mem_no());
        setBoard_idx(pushVo.getBoardIdx());
    }

    private String mem_no;
    private Integer slctPush;
    private String title;
    private String contents;
    private String imageUrl;
    private String push_type;
    private String room_no;
    private String tartget_mem_no;
    private String board_idx;
}

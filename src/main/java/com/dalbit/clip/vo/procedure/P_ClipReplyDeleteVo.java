package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipReplyDeleteVo;
import com.dalbit.clip.vo.request.ClipReplyEditVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipReplyDeleteVo {

    private String mem_no;
    private String cast_no;
    private int board_idx;

    public P_ClipReplyDeleteVo(){}
    public P_ClipReplyDeleteVo(ClipReplyDeleteVo clipReplyDeleteVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipReplyDeleteVo.getClipNo());
        setBoard_idx(clipReplyDeleteVo.getReplyIdx());
    }
}

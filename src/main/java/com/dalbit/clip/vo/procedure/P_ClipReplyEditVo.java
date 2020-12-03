package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipReplyEditVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipReplyEditVo extends P_ApiVo {

    private String mem_no;
    private String cast_no;
    private int board_idx;
    private String contents;

    public P_ClipReplyEditVo(){}
    public P_ClipReplyEditVo(ClipReplyEditVo clipReplyEditVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipReplyEditVo.getClipNo());
        setBoard_idx(clipReplyEditVo.getReplyIdx());
        setContents(clipReplyEditVo.getContents());
    }
}

package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipAddVo;
import com.dalbit.clip.vo.request.ClipReplyAddVo;
import com.dalbit.common.vo.DeviceVo;
import com.dalbit.member.vo.MemberVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipReplyAddVo {

    private String cast_no;
    private String mem_no;
    private String contents;

    public P_ClipReplyAddVo(){}
    public P_ClipReplyAddVo(ClipReplyAddVo clipReplyAddVo, HttpServletRequest request){
        setCast_no(clipReplyAddVo.getClipNo());
        setMem_no(MemberVo.getMyMemNo(request));
        setContents(clipReplyAddVo.getContents());
    }
    
}

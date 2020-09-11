package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipPlayListVo;
import com.dalbit.clip.vo.request.ClipPlayVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipPlayVo {

    private int memLogin;
    private String mem_no;
    private String cast_no;

    public P_ClipPlayVo(){}
    public P_ClipPlayVo(ClipPlayVo clipPlayVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipPlayVo.getClipNo());
    }

    public P_ClipPlayVo(P_ClipReplyListVo pClipReplyListVo, HttpServletRequest request){
        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(pClipReplyListVo.getCast_no());
    }
    
}

package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeReplyEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MypageNoticeReplyEditVo extends P_ApiVo {

    private String star_mem_no;
    private String edit_mem_no;
    private int reply_idx;
    private String contents;

    public P_MypageNoticeReplyEditVo(){}
    public P_MypageNoticeReplyEditVo(MypageNoticeReplyEditVo mypageNoticeReplyEditVo, HttpServletRequest request){
        setStar_mem_no(mypageNoticeReplyEditVo.getMemNo());
        setEdit_mem_no(MemberVo.getMyMemNo(request));
        setReply_idx(mypageNoticeReplyEditVo.getReplyIdx());
        setContents(mypageNoticeReplyEditVo.getContents());
    }
}

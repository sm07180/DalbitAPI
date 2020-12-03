package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeReplyDeleteVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MypageNoticeReplyDeleteVo extends P_ApiVo {

    private String star_mem_no;
    private String delete_mem_no;
    private int reply_idx;

    public P_MypageNoticeReplyDeleteVo(){}
    public P_MypageNoticeReplyDeleteVo(MypageNoticeReplyDeleteVo mypageNoticeReplyDeleteVo, HttpServletRequest request){
        setStar_mem_no(mypageNoticeReplyDeleteVo.getMemNo());
        setDelete_mem_no(MemberVo.getMyMemNo(request));
        setReply_idx(mypageNoticeReplyDeleteVo.getReplyIdx());
    }
}

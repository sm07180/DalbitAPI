package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeReplyAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MypageNoticeReplyAddVo {

    private String star_mem_no;
    private String writer_mem_no;
    private int notice_no;
    private String contents;

    public P_MypageNoticeReplyAddVo(){}
    public P_MypageNoticeReplyAddVo(MypageNoticeReplyAddVo mypageNoticeReplyAddVo, HttpServletRequest request){
        setStar_mem_no(mypageNoticeReplyAddVo.getMemNo());
        setWriter_mem_no(MemberVo.getMyMemNo(request));
        setNotice_no(mypageNoticeReplyAddVo.getNoticeIdx());
        setContents(mypageNoticeReplyAddVo.getContents());
    }
}

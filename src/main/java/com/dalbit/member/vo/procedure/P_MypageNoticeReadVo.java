package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeReadVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageNoticeReadVo {
    private String mem_no;
    private Integer noticeIdx;

    public P_MypageNoticeReadVo(MypageNoticeReadVo mypageNoticeReadVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setNoticeIdx(mypageNoticeReadVo.getNoticeIdx());
    }
}

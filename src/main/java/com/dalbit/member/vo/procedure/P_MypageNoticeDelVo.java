package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeDelVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageNoticeDelVo {

    public P_MypageNoticeDelVo(MypageNoticeDelVo mypageNoticeDelVo, HttpServletRequest request){
        setMem_no(new MemberVo().getMyMemNo(request));
        setTarget_mem_no(mypageNoticeDelVo.getMemNo());
        setNoticeIdx(mypageNoticeDelVo.getNoticeIdx());
    }

    private String mem_no;
    private String target_mem_no;
    private Integer noticeIdx;

}

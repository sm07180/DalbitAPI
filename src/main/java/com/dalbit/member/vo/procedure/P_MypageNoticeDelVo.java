package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeDelVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageNoticeDelVo extends P_ApiVo {

    public P_MypageNoticeDelVo(MypageNoticeDelVo mypageNoticeDelVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setTarget_mem_no(mypageNoticeDelVo.getMemNo());
        setNoticeIdx(mypageNoticeDelVo.getNoticeIdx());
    }

    private String mem_no;
    private String target_mem_no;
    private Integer noticeIdx;

}

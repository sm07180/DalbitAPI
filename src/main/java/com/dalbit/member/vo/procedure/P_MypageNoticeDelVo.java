package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeDelVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageNoticeDelVo {

    public P_MypageNoticeDelVo(MypageNoticeDelVo mypageNoticeDelVo){
        setMem_no(MemberVo.getMyMemNo());
        setTarget_mem_no(mypageNoticeDelVo.getMemNo());
        setNoticeIdx(mypageNoticeDelVo.getNoticeIdx());
    }

    private String mem_no;
    private String target_mem_no;
    private Integer noticeIdx;

}

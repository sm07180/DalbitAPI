package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeAddVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageNoticeAddVo {

    public P_MypageNoticeAddVo(MypageNoticeAddVo mypageNoticeAddVo){
        int topFix = (mypageNoticeAddVo.getTopFix().equals("1") || mypageNoticeAddVo.getTopFix().toUpperCase().equals("TRUE")) ? 1 : 0;

        setMem_no(MemberVo.getMyMemNo());
        setTarget_mem_no(mypageNoticeAddVo.getMemNo());
        setContents(mypageNoticeAddVo.getContents());
        setTitle(mypageNoticeAddVo.getTitle());
        setTopFix(topFix);

    }

    private String mem_no;
    private String target_mem_no;
    private String title;
    private String contents;
    private int topFix;
}

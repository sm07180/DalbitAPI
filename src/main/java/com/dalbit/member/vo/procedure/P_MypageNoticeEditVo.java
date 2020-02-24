package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeEditVo;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class P_MypageNoticeEditVo {

    public P_MypageNoticeEditVo(MypageNoticeEditVo mypageNoticeEditVo){
        int topFix = (mypageNoticeEditVo.getTopFix().equals("1") || mypageNoticeEditVo.getTopFix().toUpperCase().equals("TRUE")) ? 1 : 0;

        setMem_no(MemberVo.getMyMemNo());
        setTarget_mem_no(mypageNoticeEditVo.getMemNo());
        setNoticeIdx(mypageNoticeEditVo.getNoticeIdx());
        setTitle(mypageNoticeEditVo.getTitle());
        setContents(mypageNoticeEditVo.getContents());
        setTopFix(topFix);
    }

    private String mem_no;
    private String target_mem_no;
    private Integer noticeIdx;
    private String title;
    private String contents;
    private int topFix;

}

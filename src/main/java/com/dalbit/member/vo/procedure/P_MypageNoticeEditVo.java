package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageNoticeEditVo {

    public P_MypageNoticeEditVo(MypageNoticeEditVo mypageNoticeEditVo, HttpServletRequest request){
        int topFix = (mypageNoticeEditVo.getIsTop().equals("1") || mypageNoticeEditVo.getIsTop().toUpperCase().equals("TRUE")) ? 1 : 0;

        setMem_no(new MemberVo().getMyMemNo(request));
        setTarget_mem_no(mypageNoticeEditVo.getMemNo());
        setNoticeIdx(mypageNoticeEditVo.getNoticeIdx());
        setTitle(mypageNoticeEditVo.getTitle());
        setContents(mypageNoticeEditVo.getContents());
        setImagePath(mypageNoticeEditVo.getImagePath());
        setTopFix(topFix);
    }

    private String mem_no;
    private String target_mem_no;
    private Integer noticeIdx;
    private String title;
    private String contents;
    private String imagePath;
    private int topFix;

}

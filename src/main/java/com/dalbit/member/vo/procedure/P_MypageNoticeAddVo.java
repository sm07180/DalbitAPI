package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_MypageNoticeAddVo {

    public P_MypageNoticeAddVo(MypageNoticeAddVo mypageNoticeAddVo, HttpServletRequest request){
        int topFix = (mypageNoticeAddVo.getIsTop().equals("1") || mypageNoticeAddVo.getIsTop().toUpperCase().equals("TRUE")) ? 1 : 0;

        setMem_no(new MemberVo().getMyMemNo(request));
        setTarget_mem_no(mypageNoticeAddVo.getMemNo());
        setContents(mypageNoticeAddVo.getContents());
        setImagePath(mypageNoticeAddVo.getImagePath());
        setTitle(mypageNoticeAddVo.getTitle());
        setTopFix(topFix);

    }

    private String mem_no;
    private String target_mem_no;
    private String title;
    private String contents;
    private String imagePath;
    private int topFix;
}

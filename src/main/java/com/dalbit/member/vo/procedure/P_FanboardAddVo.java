package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanboardAddVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_FanboardAddVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String writer_mem_no;   // 댓글 작성자 회원번호
    private Integer depth;          // 1. 댓글 2. 대댓글
    private Integer board_no;       // 대댓글인경우 댓글의 그룹번호
    private String contents;        // 댓글내용
    private Integer viewOn;         // 1: 공개글, 0: 비밀글

    public P_FanboardAddVo(){}
    public P_FanboardAddVo(FanboardAddVo fanboardAddVo, HttpServletRequest request){
        setStar_mem_no(fanboardAddVo.getMemNo());
        setWriter_mem_no(MemberVo.getMyMemNo(request));
        setDepth(fanboardAddVo.getDepth());
        setBoard_no(fanboardAddVo.getParentGroupIdx());
        setContents(fanboardAddVo.getContents());
        setViewOn(fanboardAddVo.getViewOn());
    }

}

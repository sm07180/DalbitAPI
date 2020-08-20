package com.dalbit.member.vo.procedure;


import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanboardDelVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_FanboardDeleteVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String delete_mem_no;   // 댓글 삭제자 회원번호
    private int board_idx;          // 댓글 인덱스번호

    public P_FanboardDeleteVo(){}
    public P_FanboardDeleteVo(FanboardDelVo fanboardDelVo, HttpServletRequest request){
        setStar_mem_no(fanboardDelVo.getMemNo());
        setDelete_mem_no(new MemberVo().getMyMemNo(request));
        setBoard_idx(fanboardDelVo.getBoardIdx());
    }
}

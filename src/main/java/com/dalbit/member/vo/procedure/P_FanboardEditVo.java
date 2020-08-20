package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanboardEditVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_FanboardEditVo {

    /* input */
    private String star_mem_no;     // 팬보드 스타 회원번호
    private String edit_mem_no;   // 댓글 수정자 회원번호
    private Integer board_idx;       // 댓글 인덱스 번호
    private String contents;        // 댓글내용
    
    public P_FanboardEditVo(){}
    public P_FanboardEditVo(FanboardEditVo fanboardEditVo, HttpServletRequest request){
        setStar_mem_no(fanboardEditVo.getMemNo());
        setEdit_mem_no(MemberVo.getMyMemNo(request));
        setBoard_idx(fanboardEditVo.getBoardIdx());
        setContents(fanboardEditVo.getContent());
    }
}

package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanboardReplyVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class P_FanboardReplyVo extends P_ApiVo {

    /* input */
    private String mem_no;          // 팬보드 댓글 리스트 요청 회원번호
    private String star_mem_no;     // 팬보드 스타 회원번호
    private Integer board_no;       // 댓글 그룹번호

    /* Output */
    private int board_idx;          // 댓글 인덱스번호
    private String writer_mem_no;   // 작성자 회원번호
    private String nickName;        // 작성자 닉네임
    private String memSex;          // 작성자 성별
    private String profileImage;    // 작성자 프로필 이미지
    private String contents;        // 댓글내용
    private int status;             // 상태값 1. 정상 2. 삭제됨
    private Date writeDate;         // 작성일자
    private String userId;          // 회원 아이디
    private int viewOn;

    public P_FanboardReplyVo(){}
    public P_FanboardReplyVo(FanboardReplyVo fanboardReplyVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setStar_mem_no(fanboardReplyVo.getMemNo());
        setBoard_no(fanboardReplyVo.getReplyIdx());
    }

}

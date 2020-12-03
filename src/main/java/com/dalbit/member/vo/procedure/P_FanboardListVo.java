package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.FanboardViewVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter
@Setter
public class P_FanboardListVo extends P_ApiVo {

    public P_FanboardListVo(){}
    public P_FanboardListVo(FanboardViewVo fanboardViewVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(fanboardViewVo.getPage()) ? 1 : fanboardViewVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(fanboardViewVo.getRecords()) ? 10 : fanboardViewVo.getRecords();

        setMem_no(new MemberVo().getMyMemNo(request));
        setStar_mem_no(fanboardViewVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }
    /* Input */
    private String mem_no;          // 팬보드 댓글 리스트 요청 회원번호
    private String star_mem_no;     // 팬보드 스타 회원번호
    private Integer pageNo;         // 페이징 현재 번호
    private Integer pageCnt;        // 페이징 당 개수

    /* Output */
    private int board_idx;          // 댓글 인덱스 번호
    private int board_no;           // 댓글 그룹 번호
    private String writer_mem_no;   // 작성자 회원번호
    private String nickName;        // 작성자 닉네임
    private String memSex;          // 작성자 성별
    private String profileImage;    // 작성자 프로필 이미지
    private String contents;        // 댓글내용
    private int replyCnt;           // 대댓글 총 개수
    private int status;             // 상태값 1. 정상 2. 비정상
    private Date writeDate;         // 작성일자
    private String userId;          // 회원 아이디
    private int viewOn;             // 비밀여부 1:공개글, 0:비밀글

}
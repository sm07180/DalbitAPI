package com.dalbit.member.vo.procedure;

import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.MypageNoticeReplyListVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter
@Setter
public class P_MypageNoticeReplyListVo extends P_ApiVo {

    public P_MypageNoticeReplyListVo(){}
    public P_MypageNoticeReplyListVo(MypageNoticeReplyListVo mypageNoticeReplyListVo, HttpServletRequest request){
        setPageNo(DalbitUtil.isEmpty(mypageNoticeReplyListVo.getPage()) ? 1 : mypageNoticeReplyListVo.getPage());
        setPageCnt(DalbitUtil.isEmpty(mypageNoticeReplyListVo.getRecords()) ? 10 : mypageNoticeReplyListVo.getRecords());
        setStar_mem_no(mypageNoticeReplyListVo.getMemNo());
        setMem_no(MemberVo.getMyMemNo(request));
        setNotice_no(mypageNoticeReplyListVo.getNoticeIdx());
    }

    /* input */
    private String mem_no;
    private String star_mem_no;
    private int notice_no;
    private int pageNo;
    private int pageCnt;

    /* output */
    private int reply_idx;          // 댓글 인덱스
    private String writer_mem_no;   // 작성자 회원번호
    private String nickName;        // 작성자 닉네임
    private String userId;          // 작성자 user id
    private String memSex;          // 작성자 성별
    private String profileImage;    // 작성자 프로필 이미지
    private String contents;        // 댓글 내용
    private String writeDate;       // 작성 일자
}

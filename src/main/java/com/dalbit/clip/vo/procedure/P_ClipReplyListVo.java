package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipReplyListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_ClipReplyListVo {

    public P_ClipReplyListVo(){}
    public P_ClipReplyListVo(ClipReplyListVo clipReplyListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipReplyListVo.getPage()) ? 1 : clipReplyListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipReplyListVo.getRecords()) ? 10 : clipReplyListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setCast_no(clipReplyListVo.getClipNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;
    private String cast_no;
    private int pageNo;
    private int pageCnt;

    /* Output */
    private int board_idx;              //댓글번호
    private String cast_mem_no;         //캐스트 회원번호
    private String writer_mem_no;       //작성자 회원번호
    private String memId;               //작성자 아이디
    private String nickName;            //작성자 닉네임
    private String memSex;              //작성자 성별
    private String profileImage;        //프로필이미지
    private String contents;            //댓글내용
    private Date writeDate;             //작성일자


}

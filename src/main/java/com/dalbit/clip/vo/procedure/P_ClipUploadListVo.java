package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipUploadListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipUploadListVo {

    public P_ClipUploadListVo(){}
    public P_ClipUploadListVo(ClipUploadListVo clipUploadListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipUploadListVo.getPage()) ? 1 : clipUploadListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipUploadListVo.getRecords()) ? 10 : clipUploadListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setStar_mem_no(clipUploadListVo.getMemNo());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;          //요청회원번호
    private String star_mem_no;     //스타회원번호
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String cast_no;              //클립 번호
    private String subject_type;        //클립 주제
    private String title;               //클립 제목
    private String image_background;    //클립 배경이미지
    private int playCnt;                //재생 수
    private int goodCnt;                //좋아요 수
    private int giftCnt;                //받은별 수
    private String nickName;            //닉네임
    private int replyCnt;               //댓글 수

}

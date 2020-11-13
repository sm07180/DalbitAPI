package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipListenListVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;

@Getter @Setter
public class P_ClipListenListVo {
    public P_ClipListenListVo(){}
    public P_ClipListenListVo(ClipListenListVo clipListenListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipListenListVo.getPage()) ? 1 : clipListenListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipListenListVo.getRecords()) ? 10 : clipListenListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setStar_mem_no(clipListenListVo.getMemNo());
        setSlctType(clipListenListVo.getSlctType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;          //요청회원번호
    private String star_mem_no;     //스타회원번호
    private int slctType;
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
    private String filePlayTime;
    private String memSex;
    private int badge_newdj;                //신입뱃지
    private int badge_special;              //스페셜뱃지
}

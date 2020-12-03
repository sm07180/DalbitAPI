package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipPlayListVo;
import com.dalbit.common.vo.P_ApiVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Getter @Setter
public class P_ClipPlayListVo extends P_ApiVo {

    public P_ClipPlayListVo(){}
    public P_ClipPlayListVo(ClipPlayListVo clipPlayListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipPlayListVo.getPage()) ? 1 : clipPlayListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipPlayListVo.getRecords()) ? 10 : clipPlayListVo.getRecords();

        setMem_no(MemberVo.getMyMemNo(request));
        setSortType(clipPlayListVo.getSortType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private String mem_no;          //요청회원번호
    private int sortType;           //정렬구분 (0: 사용자설정[default], 1: 재생순, 2: 좋아요순, 3: 선물순, 4: 제목순, 5: 추가순)
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String cast_no;              //클립 번호
    private String subject_type;        //클립 주제
    private String title;               //클립 제목
    private String image_background;    //클립 배경이미지
    private String filePlayTime;        //재생시간
    private String nickName;            //닉네임
    private String memSex;              //성별
    private int playCnt;                //재생 수
    private int goodCnt;                //좋아요 수
    private int giftCnt;                //받은별 수
    private Date ins_date;              //추가 일자

}

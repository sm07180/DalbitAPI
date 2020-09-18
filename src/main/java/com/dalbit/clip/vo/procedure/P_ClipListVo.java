package com.dalbit.clip.vo.procedure;

import com.dalbit.clip.vo.request.ClipListVo;
import com.dalbit.member.vo.MemberVo;
import com.dalbit.util.DalbitUtil;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_ClipListVo {

    public P_ClipListVo(){}
    public P_ClipListVo(ClipListVo clipListVo, HttpServletRequest request){
        int pageNo = DalbitUtil.isEmpty(clipListVo.getPage()) ? 1 : clipListVo.getPage();
        int pageCnt = DalbitUtil.isEmpty(clipListVo.getRecords()) ? 10 : clipListVo.getRecords();

        setMemLogin(DalbitUtil.isLogin(request) ? 1 : 0);
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(clipListVo.getSlctType());
        setSubjectType(clipListVo.getSubjectType());
        setGender(clipListVo.getGender());
        setDjType(clipListVo.getDjType());
        setPageNo(pageNo);
        setPageCnt(pageCnt);
    }

    /* InPut */
    private int memLogin;           //회원 로그인 상태(1: 회원, 0: 비회원)
    private String mem_no;          //요청회원번호
    private int slctType;           //검색구분(0: 전체, 1: 추천, 2: 재생수, 3: 좋아요)
    private String subjectType;     //클립 주제 (01~99)
    private String gender;          //성별(null: 전체, m: 남성, f: 여성)
    private int djType;             //신입구분(0: 전체, 1: 신입)
    private int pageNo;
    private int pageCnt;

    /* Output */
    private String cast_no;              //클립 번호
    private String subject_type;        //클립 주제
    private String title;               //클립 제목
    private String image_background;    //클립 배경이미지
    private int type_entry;             //청취제한
    private int count_play;             //재생 수
    private int count_good;             //좋아요 수
    private int count_byeol;            //받은별 수
    private double total;               //순위점수
    private String filePlayTime;        //재생시간
    private int os_type;                //1: aos, 2: ios, 3: pc
    private int badge_newdj;            //신입뱃지(1)
    private int badge_special;          //스페셜Dj(1)
    private String nickName;            //닉네임
    private String memSex;              //성별
    private int birthYear;              //생년
    private String profileImage;        //프로필이미지
    private int replyCnt;               //댓글 수


}

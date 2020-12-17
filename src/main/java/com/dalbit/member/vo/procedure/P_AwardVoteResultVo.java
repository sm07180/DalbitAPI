package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.AwardVoteResultVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_AwardVoteResultVo {
    public P_AwardVoteResultVo(){}
    public P_AwardVoteResultVo(AwardVoteResultVo awardVoteResultVo, HttpServletRequest request){
        setMem_no(MemberVo.getMyMemNo(request));
        setSlctType(awardVoteResultVo.getSlctType());
        setSelectYear(awardVoteResultVo.getYear());
    }
    /* Input */
    private String mem_no;
    private int slctType;
    private String selectYear;

    /* Output */
    private int rank;
    private int voteCnt;
    private String dj_memNo;
    private String dj_nickName;
    private String dj_memSex;
    private String dj_profileImage;
    private String fan1_memNo;
    private String fan1_nickName;
    private String fan1_memSex;
    private String fan1_profileImage;
    private String fan2_memNo;
    private String fan2_nickName;
    private String fan2_memSex;
    private String fan2_profileImage;
    private String fan3_memNo;
    private String fan3_nickName;
    private String fan3_memSex;
    private String fan3_profileImage;
}

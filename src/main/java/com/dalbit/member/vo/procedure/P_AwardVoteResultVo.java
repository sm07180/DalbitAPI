package com.dalbit.member.vo.procedure;

import com.dalbit.member.vo.MemberVo;
import com.dalbit.member.vo.request.AwardVoteResultVo;
import lombok.Getter;
import lombok.Setter;

import javax.servlet.http.HttpServletRequest;

@Getter @Setter
public class P_AwardVoteResultVo {
    public P_AwardVoteResultVo(){}
    public P_AwardVoteResultVo(AwardVoteResultVo awardVoteResultVo){
        setSlctType(awardVoteResultVo.getSlctType());
        setSelectYear(awardVoteResultVo.getYear());
    }
    /* Input */
    private int slctType;
    private String selectYear;

    /* Output */
    private int rowNum;
    private String memNo;
    private String memNick;
    private String memSex;
    private String profileImage;
    private String fan1_memNo;
    private String fan1_memNick;
    private String fan1_memSex;
    private String fan1_profileImage;
    private String fan2_memNo;
    private String fan2_memNick;
    private String fan2_memSex;
    private String fan2_profileImage;
    private String fan3_memNo;
    private String fan3_memNick;
    private String fan3_memSex;
    private String fan3_profileImage;
}
